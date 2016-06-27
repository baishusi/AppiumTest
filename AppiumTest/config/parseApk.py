#!/usr/bin/python
#-*- coding:utf-8 -*-
import os
import shutil
import xml.dom.minidom
import sys,re

reload(sys)  
sys.setdefaultencoding('utf8') 


#解析apk，得到包名
def getpackagename(apkpath):
	os.system("java -jar apktool_2.0.2.jar d "+apkpath)
	dom = xml.dom.minidom.parse(shotname+'/AndroidManifest.xml')
	root = dom.documentElement
	return root.getAttribute("package")

if __name__=='__main__': 
	data=open("config.txt")
	for each_line in data:
		each_line = re.sub('\n','',each_line)
		left=each_line.split("=")[0]
		right=each_line.split("=")[1]
		if left == 'androidApkPath':
			androidapkpath=right
			break
	data.close()
	# androidapkpath=u'E:\\apptium\\appiumpython\\xyjhtest\\apk'
	for root,dirs,files in os.walk(androidapkpath):
		for filename in files:
			if filename.endswith('.apk'):
				srcfullpath=os.path.join(root, filename)
				dstfullpath=os.path.join(root,filename.split("_")[-1])
				shutil.move(srcfullpath,dstfullpath)


	configfile=file('apkconfig.txt',"w")#获取各个apk的文件名和包名写入apkconfig文件中
	for root,dirs,files in os.walk(androidapkpath): 
		for filename in files:
			if filename.endswith('.apk'):
				(shotname,extension) = os.path.splitext(filename)
				if os.path.exists(shotname):
					shutil.rmtree(shotname)
				filefullpath=os.path.join(root, filename)
				filefullpath = filefullpath.decode('utf-8').encode('gb2312') 
				packagename=getpackagename(filefullpath)
				configfile.write(filefullpath+"	"+packagename+"\n")
				shutil.rmtree(shotname)

	configfile.close()