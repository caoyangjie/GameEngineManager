# -*- coding:utf-8 -*-
"""
将指定B站视频链接(支持BV号/av号/多P合集)的标题与播放页地址导出到
`GameEngineManager/backend/src/main/resources/video.json`。

用法示例:
    python export_video_list.py --url https://www.bilibili.com/video/BV1xx411k7bk
    python export_video_list.py --url 49842011   # 直接输入av号

注意:
- 仅抓取播放页地址, 不做视频下载。
- 仅适配普通稿件(多P合集); 番剧/影视等PGC暂未处理。
"""

import argparse
import json
import os
import re
import sys
from datetime import datetime, timezone
from typing import Dict, List, Optional

import requests

# 目标输出文件(使用绝对路径便于在不同工作目录运行)
DEFAULT_OUTPUT = (
    "/hxcloud/ai/cursor_workspace/TemplateProject/"
    "GameEngineManager/backend/src/main/resources/video.json"
)


class BilibiliCollectError(Exception):
    """自定义异常, 便于调用方识别错误。"""


def parse_identity(url_or_id: str) -> Dict[str, str]:
    """
    根据输入提取aid或bvid.
    允许的输入:
    - 完整播放页URL: https://www.bilibili.com/video/BVxxxx?p=2
    - 含av/BV的URL
    - 纯数字(视为aid/av号)
    """
    text = url_or_id.strip()
    bv_match = re.search(r"(BV[0-9A-Za-z]{10})", text)
    if bv_match:
        return {"bvid": bv_match.group(1)}

    av_match = re.search(r"/av(\d+)", text, re.IGNORECASE)
    if av_match:
        return {"aid": av_match.group(1)}

    if text.isdigit():
        return {"aid": text}

    raise BilibiliCollectError("无法识别输入中的BV号或av号")


def fetch_video_pages(identity: Dict[str, str], sess: requests.Session) -> Dict[str, List[Dict[str, str]]]:
    """
    调用B站稿件详情接口, 返回包含bvid和分P列表的结构。
    """
    api = "https://api.bilibili.com/x/web-interface/view"
    resp = sess.get(api, params=identity, timeout=10)
    if resp.status_code != 200:
        if resp.status_code == 412:
            raise BilibiliCollectError("HTTP 412: B站接口触发风控，请稍后重试或更换网络/UA")
        raise BilibiliCollectError(f"调用接口失败, HTTP {resp.status_code}")
    payload = resp.json()
    if payload.get("code") != 0:
        raise BilibiliCollectError(f"接口返回错误: {payload.get('message')}")

    data = payload.get("data") or {}
    bvid = data.get("bvid")
    pages = data.get("pages") or []
    if not bvid or not pages:
        raise BilibiliCollectError("未获取到bvid或分P信息")

    videos = []
    for page in pages:
        page_no = page.get("page")
        part_title = page.get("part") or f"P{page_no}"
        videos.append(
            {
                "title": part_title,
                "url": f"https://www.bilibili.com/video/{bvid}?p={page_no}",
            }
        )
    return {"bvid": bvid, "videos": videos}


def write_video_json(
    videos: List[Dict[str, str]],
    source: str,
    output_path: str = DEFAULT_OUTPUT,
) -> None:
    """
    将采集结果写入JSON文件。
    格式:
    {
        "source": "<用户输入的链接或ID>",
        "generated_at": "<ISO时间>",
        "videos": [
            {"title": "...", "url": "..."},
            ...
        ]
    }
    """

    """ 
    请先读取目标文件中的内容,然后与当前传入的json合并后一起写入目标文件
    """
    if os.path.exists(output_path):
        with open(output_path, "r", encoding="utf-8") as f:
            existing_json = json.load(f)
    else:
        existing_json = {
            "source": source,
            "generated_at": datetime.now(timezone.utc).isoformat(),
            "videos": videos,
        }
    # 确保 existing_json 中有 "videos" 键
    if "videos" not in existing_json:
        existing_json["videos"] = []

    # 合并 existing_videos 和 videos
    existing_videos = existing_json["videos"]
    existing_videos.extend(videos)  # 将新的视频合并到现有的视频列表中
    
    # 将合并后的结果写回到目标文件中
    with open(output_path, "w", encoding="utf-8") as f:
        json.dump(existing_json, f, ensure_ascii=False, indent=2)


def run(url: str, output: Optional[str]) -> str:
    output_path = output or DEFAULT_OUTPUT
    sess = requests.Session()
    # 设置常见请求头以避免B站接口返回412
    sess.headers.update(
        {
            "User-Agent": (
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                "AppleWebKit/537.36 (KHTML, like Gecko) "
                "Chrome/122.0.0.0 Safari/537.36"
            ),
            "Referer": "https://www.bilibili.com",
            "Origin": "https://www.bilibili.com",
            "Accept": "application/json, text/plain, */*",
            "Accept-Language": "zh-CN,zh;q=0.9",
            "Connection": "keep-alive",
        }
    )
    identity = parse_identity(url)
    result = fetch_video_pages(identity, sess)
    write_video_json(result["videos"], url, output_path)
    return output_path


def main(argv: Optional[List[str]] = None) -> None:
    parser = argparse.ArgumentParser(description="导出B站合集视频标题与播放地址到video.json")
    parser.add_argument("--url", required=True, help="B站播放链接或av/BV号")
    parser.add_argument("--output", help="自定义输出文件路径, 默认写入后端resources/video.json")
    args = parser.parse_args(argv)

    try:
        output_path = run(args.url, args.output)
        print(f"已写入: {output_path}")
    except BilibiliCollectError as e:
        print(f"错误: {e}")
        sys.exit(1)
    except Exception as e:
        print(f"未知错误: {e}")
        sys.exit(1)


if __name__ == "__main__":
    main()

