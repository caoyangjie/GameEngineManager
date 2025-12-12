#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
图片分割脚本
将4x4网格图片分割成16个单独的图片文件
支持去除图片左侧数字的功能
"""
import sys
from PIL import Image
import os
import glob

def split_image_grid(input_path, output_dir, cell_num, rows=4, cols=4):
    """
    将图片分割成网格
    
    Args:
        input_path: 输入图片路径
        output_dir: 输出目录
        rows: 行数（默认4）
        cols: 列数（默认4）
    """
    # 检查输入文件是否存在
    if not os.path.exists(input_path):
        print(f"错误: 找不到输入文件: {input_path}")
        return False
    
    # 创建输出目录
    os.makedirs(output_dir, exist_ok=True)
    
    try:
        # 打开图片
        img = Image.open(input_path)
        width, height = img.size
        
        # 计算每个单元格的尺寸
        cell_width = width // cols
        cell_height = height // rows
        
        print(f"原图尺寸: {width}x{height}")
        print(f"每个单元格尺寸: {cell_width}x{cell_height}")
        
        # 分割图片
        for row in range(rows):
            for col in range(cols):
                # 计算裁剪区域
                left = col * cell_width
                top = row * cell_height
                right = left + cell_width
                bottom = top + cell_height
                
                # 裁剪图片
                cell_img = img.crop((left, top, right, bottom))
                
                # 保存图片
                output_path = os.path.join(output_dir, f"{cell_num}.png")
                cell_img.save(output_path, "PNG")
                print(f"已保存: {output_path} (单元格 {cell_num})")
                
                cell_num += 1
        
        print(f"\n完成! 共分割 {cell_num} 个图片到 {output_dir}")
        return True
        
    except Exception as e:
        print(f"错误: {str(e)}")
        return False

def remove_left_number(input_dir, output_dir=None, crop_width=None):
    """
    去除图片左侧的数字部分
    
    Args:
        input_dir: 输入图片目录路径
        output_dir: 输出目录路径（如果为None，则覆盖原文件）
        crop_width: 要裁剪的左侧宽度（像素），如果为None则自动检测
    """
    if not os.path.exists(input_dir):
        print(f"错误: 找不到输入目录: {input_dir}")
        return False
    
    # 如果未指定输出目录，则覆盖原文件
    if output_dir is None:
        output_dir = input_dir
    else:
        os.makedirs(output_dir, exist_ok=True)
    
    # 获取所有PNG图片
    image_files = glob.glob(os.path.join(input_dir, "*.png"))
    if not image_files:
        print(f"警告: 在 {input_dir} 中未找到PNG图片")
        return False
    
    print(f"找到 {len(image_files)} 个图片文件")
    
    # 如果未指定裁剪宽度，尝试从第一张图片自动检测
    if crop_width is None:
        try:
            sample_img = Image.open(image_files[0])
            width, height = sample_img.size
            # 假设左侧数字区域占图片宽度的20-30%，这里使用25%作为默认值
            # 用户可以根据实际情况调整
            crop_width = int(width * 0.20)
            print(f"自动检测到裁剪宽度: {crop_width} 像素（图片宽度的25%）")
            print("提示: 如果裁剪不准确，可以使用 --crop-width 参数手动指定")
        except Exception as e:
            print(f"错误: 无法打开示例图片进行检测: {str(e)}")
            return False
    
    success_count = 0
    for img_path in image_files:
        try:
            img = Image.open(img_path)
            width, height = img.size
            
            # 裁剪掉左侧部分
            left = crop_width
            top = 0
            right = width
            bottom = height
            
            cropped_img = img.crop((left, top, right, bottom))
            
            # 保存图片
            filename = os.path.basename(img_path)
            output_path = os.path.join(output_dir, filename)
            cropped_img.save(output_path, "PNG")
            print(f"已处理: {filename} (裁剪左侧 {crop_width} 像素)")
            success_count += 1
            
        except Exception as e:
            print(f"错误: 处理 {os.path.basename(img_path)} 时出错: {str(e)}")
    
    print(f"\n完成! 成功处理 {success_count}/{len(image_files)} 个图片")
    return True

def crop_image_to_content(img):
    """
    去除图片的白色边界

    Args:
        img: 输入的图像对象

    Returns:
        裁剪后的图像对象
    """
    # 转为灰度图像，检测白色边界
    gray_img = img.convert("L")
    threshold = 240  # 设定阈值，白色区域的阈值
    bw = gray_img.point(lambda x: 255 if x > threshold else 0, '1')

    # 确定非白色区域的边界
    bbox = bw.getbbox()
    if bbox:
        return img.crop(bbox)  # 裁剪到图像内容
    else:
        return img  # 如果没有找到边界，返回原图

def split_playing_cards(input_path, output_dir, cell_num, rows=4, cols=10, card_width=80, card_height=120):
    """
    切割扑克牌图像
    
    Args:
        input_path: 输入图片路径
        output_dir: 输出目录
        rows: 行数（默认4）
        cols: 列数（默认10）
        card_width: 每张牌的宽度（像素）
        card_height: 每张牌的高度（像素）
    """
    if not os.path.exists(input_path):
        print(f"错误: 找不到输入文件: {input_path}")
        return False
    
    os.makedirs(output_dir, exist_ok=True)
    
    try:
        img = Image.open(input_path)
        print(f"原图尺寸: {img.size}")
        img = crop_image_to_content(img)  # 去除白色边界
        print(f"裁剪后的图像尺寸: {img.size}")
        
        for row in range(rows):
            for col in range(cols):
                left = col * card_width
                top = row * card_height
                right = left + card_width
                bottom = top + card_height
                
                cell_img = img.crop((left, top, right, bottom))
                output_path = os.path.join(output_dir, f"card_{cell_num}.png")
                cell_img.save(output_path, "PNG")
                print(f"已保存: {output_path} (牌号 {cell_num})")
                
                cell_num += 1
                
        print(f"\n完成! 共分割 {cell_num} 张牌到 {output_dir}")
        return True
        
    except Exception as e:
        print(f"错误: {str(e)}")
        return False

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("用法:")
        print("  1. 分割图片: python split_image.py <输入图片路径> [输出目录] [起始编号]")
        print("     示例: python split_image.py input.png GameEngineWeb/src/images/icons1 0")
        print("  2. 去除左侧数字: python split_image.py --remove-number <输入目录> [输出目录] [裁剪宽度]")
        print("     示例: python split_image.py --remove-number GameEngineWeb/src/images/icons")
        print("     示例: python split_image.py --remove-number GameEngineWeb/src/images/icons GameEngineWeb/src/images/icons_cleaned 100")
        print("  3. 分割扑克牌: python split_image.py --split-playing-cards <输入目录> [输出目录] [起始编号] [行数] [列数] [牌宽度] [牌高度]")
        print("     示例: python split_image.py --split-playing-cards images/扑克牌.jpeg GameEngineWeb/src/images/icons_playing_cards 0 4 10 252 338")
        sys.exit(1)
    
    # 检查是否是去除数字模式
    if sys.argv[1] == "--remove-number":
        if len(sys.argv) < 3:
            print("错误: 请指定输入目录")
            sys.exit(1)
        
        input_dir = sys.argv[2]
        output_dir = sys.argv[3] if len(sys.argv) > 3 else None
        crop_width = int(sys.argv[4]) if len(sys.argv) > 4 else None
        
        remove_left_number(input_dir, output_dir, crop_width)
    elif sys.argv[1] == "--split-playing-cards":
        if len(sys.argv) < 3:
            print("错误: 请指定输入目录")
            sys.exit(1)
        
        input_dir = sys.argv[2]
        output_dir = sys.argv[3] if len(sys.argv) > 3 else "GameEngineWeb/src/images/icons"
        cell_num = int(sys.argv[4]) if len(sys.argv) > 4 else 0
        rows = int(sys.argv[5]) if len(sys.argv) > 5 else 4
        cols = int(sys.argv[6]) if len(sys.argv) > 6 else 10
        card_width = int(sys.argv[7]) if len(sys.argv) > 7 else 80
        card_height = int(sys.argv[8]) if len(sys.argv) > 8 else 120
        split_playing_cards(input_dir, output_dir, cell_num, rows, cols, card_width, card_height)
    else:
        # 分割图片模式
        input_path = sys.argv[1]
        output_dir = sys.argv[2] if len(sys.argv) > 2 else "GameEngineWeb/src/images/icons"
        cell_num = int(sys.argv[3]) if len(sys.argv) > 3 else 0
        split_image_grid(input_path, output_dir, cell_num)

