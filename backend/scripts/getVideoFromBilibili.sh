#! /bin/bash
# 如果$2不存在，则存放到 video-timestamp.json ，否则使用video.json
if [ -z "$2" ]; then
    output=/hxcloud/ai/cursor_workspace/TemplateProject/GameEngineManager/backend/src/main/resources/video-$(date +%Y%m%d%H%M%S).json
else
    output=/hxcloud/ai/cursor_workspace/TemplateProject/GameEngineManager/backend/src/main/resources/$2
fi
python export_video_list.py --url $1 --output $output