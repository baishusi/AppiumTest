#!/usr/bin/python
#-*- coding:utf-8 -*-
import os,xlsxwriter,re
from PIL import ImageGrab,Image,ImageDraw,ImageFont
import math
import operator
import sys 
import argparse

DEFAULT_CELL_SIZE=260
DEFAULT_IMAGE_CELL_SIZE=0.3
DEFAULT_IMAGE_HEIGHT_SIZE=0.3
SHEET_NAME='result'


def transImage(theImagePath):
	imageObject = Image.open(theImagePath)
	if imageObject.size[0]<imageObject.size[1]:
		imageObject=imageObject.transpose(Image.ROTATE_90)   
		imageObject.save(theImagePath) 


def exportReport(allRootPath):
	reportfile=allRootPath+"/report.xlsx"
	picfilepath=allRootPath+'/pic/'
	testresulttxt=allRootPath+'/testresult.txt'
    
	reload(sys) 
	sys.setdefaultencoding('utf-8') 

	#写入excel
	workbook=xlsxwriter.Workbook(reportfile)
	worksheet=workbook.add_worksheet(SHEET_NAME)
	red = workbook.add_format({'bg_color':'red'})
	yellow = workbook.add_format({'bg_color':'yellow'})
	for parent,dirnames,filenames in os.walk(picfilepath):
		for filename in filenames:
			if filename.endswith('.png') or filename.endswith('.jpg'):
				picturefullpath=os.path.join(parent,filename)
				print filename
				print picturefullpath
				transImage(picturefullpath)
				worksheet.set_row(int(filename.split(".")[0].split("_")[0]),DEFAULT_CELL_SIZE)
				worksheet.insert_image(int(filename.split(".")[0].split("_")[0]),1,picturefullpath,{'positioning':2,'x_scale':DEFAULT_IMAGE_CELL_SIZE,'y_scale':DEFAULT_IMAGE_HEIGHT_SIZE})
				
	picdata=open(testresulttxt)
	rowNum=int(1)
	for each_line in picdata:
		each_line = re.sub('\n','',each_line)
		rowNum=int(each_line.split("	")[0])
		rowResult=each_line.split("	")[2]
		rowTxt=each_line.split("	")[1]
		if rowResult == 'success':
			worksheet.write(rowNum,0,rowTxt)
		elif rowResult == 'failed':
			worksheet.write(rowNum,0,rowTxt,red)
		else:
			worksheet.write(rowNum,0,rowTxt,yellow)
		rowNum += 1
	picdata.close();
	workbook.close();

def cutPic(picpath,destPath,x,y,width,high):
	im = Image.open(picpath)
	box=(int(x),int(y),int(width),int(high))
	region=im.crop(box)
	region.save(destPath)




if __name__ == '__main__':
	parser = argparse.ArgumentParser()
	parser.add_argument('-t','--type',dest='running_type',help='执行的类型 1为旋转图片 2为切割图片，3为导出报告')
	parser.add_argument('-s','--srcimagepath',dest='src_imgpath',help='原始图片地址')
	parser.add_argument('-d','--distimagepath',dest='dist_imgpath',help='目标图片地址')
	parser.add_argument('-r','--rootpath',dest='root_path',help='根目录地址')
	parser.add_argument('-x','--x',dest='x',help='x')
	parser.add_argument('-y','--y',dest='y',help='y')
	parser.add_argument('-w','--width',dest='width',help='宽')
	parser.add_argument('-hh','--high',dest='high',help='高')

	args=parser.parse_args()
	argsDict=vars(args)

	if argsDict['running_type'] == "1":
		transImage(argsDict['src_imgpath'])
	elif argsDict['running_type'] == "2":
		cutPic(argsDict['src_imgpath'],argsDict['dist_imgpath'],argsDict['x'],argsDict['y'],argsDict['width'],argsDict['high'])
	elif argsDict['running_type'] == "3":
		exportReport(argsDict['root_path'])