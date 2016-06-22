package com.yinhan.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class HelpUtil {
	
	
	public static boolean matchPic(String srcPicPath,String distPicPath)
	{
		 try {
			float percent = compare(getData(srcPicPath),getData(distPicPath));
			if(percent>60)
			{
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static void TransferPic(String picPath) {
		RunCommand("python config\\pythonhelper.py -t 1 -s "
				+ picPath);

	}

	public static void CutPic(String srcPicPath, String distPicPath, int x,
			int y, int width, int high) {
		RunCommand("python config\\pythonhelper.py -t 2 -s "
				+ srcPicPath
				+ " -d "
				+ distPicPath
				+ " -x "
				+ x
				+ " -y "
				+ y
				+ " -w " + width + " -hh " + high);

	}

	public static void ExportReport(String reportPath) {
		RunCommand("python config\\pythonhelper.py -t 3 -r "
				+ reportPath);
	}

	public static ArrayList<String[]> ReadFileManager(String filename,
			String seperator) throws Exception {
		FileInputStream fis = new FileInputStream(filename);
		
		 //可检测多种类型，并剔除bom  
		  BOMInputStream bomIn = new BOMInputStream(fis, false,ByteOrderMark.UTF_8, ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_16BE);  
		  String charset = "utf-8";  
		  //若检测到bom，则使用bom对应的编码  
		  if(bomIn.hasBOM()){  
		     charset = bomIn.getBOMCharsetName();  
		  }  
		  InputStreamReader isr = new InputStreamReader(bomIn, charset);  
		
		BufferedReader br = new BufferedReader(isr);

		String line = "";
		String[] arrs = null;
		ArrayList<String[]> seperatorResultList = new ArrayList<String[]>();
		while ((line = br.readLine()) != null) {
			if ("".equals(seperator)) {
				arrs = new String[1];
				arrs[0] = line;
			} else {
				arrs = line.split(seperator);
			}

			seperatorResultList.add(arrs);
		}
		br.close();
		isr.close();
		fis.close();
		return seperatorResultList;

	}

	public static void WriteFileManager(String filename, String writeInString,
			boolean isAppendWriter) throws Exception {
		File file = new File(filename);
		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fos = new FileOutputStream(file, isAppendWriter);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write(writeInString +Config.CHANGELINE);
		// 注意关闭的先后顺序，先打开的后关闭，后打开的先关闭
		bw.close();
		osw.close();
		fos.close();
	}

	public static String RunCommand(String command) {
		BufferedReader br2 = null;
		String line = null;
		InputStream is = null;
		InputStreamReader isReader = null;
		try {
			Process proc = Runtime.getRuntime().exec(command);
			is = proc.getInputStream();
			isReader = new InputStreamReader(is, "utf-8");
			br2 = new BufferedReader(isReader);
			while ((line = br2.readLine()) != null) {
				System.out.println("command :"+line);
				return line;
			}
		} catch (IOException e) {
			return line;
		} finally {
			if (isReader != null) {
				try {
					isReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}

			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}

			if (br2 != null) {
				try {
					br2.close();
				} catch (IOException e) {
					// TODO
				}
			}
		}
		System.out.println("command :"+line);
		return line;
	}

	public static void SnapShot(TakesScreenshot drivername, String fileFullPath) {
		// this method will take screen shot ,require two parameters ,one is
		// driver name, another is file name

		File scrFile = drivername.getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy
		// somewhere
		try {
			FileUtils
					.copyFile(scrFile, new File(fileFullPath));
			TransferPic(fileFullPath);
			
		} catch (IOException e) {
			System.out.println("Can't save screenshot");
			e.printStackTrace();
		} 
		
		
	}

	private static void deleteFile(File file) {
		if (file.exists()) {// 判断文件是否存在
			if (file.isFile()) {// 判断是否是文件
				file.delete();// 删除文件
			} else if (file.isDirectory()) {// 否则如果它是一个目录
				File[] files = file.listFiles();// 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) {// 遍历目录下所有的文件
					deleteFile(files[i]);// 把每个文件用这个方法进行迭代
				}
				file.delete();// 删除文件夹
			}
		} else {
			System.out.println("所删除的文件不存在");
		}
	}

	public static void RemoveAndMakeDir(String path) {
		File file = new File(path);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdir();
		} else {
			deleteFile(file);
			file.mkdir();
		}
	}

	public static String GetFileName(String path) {

		File tempFile = new File(path.trim());

		String fileName = tempFile.getName();

		return fileName;
	}
	
    public static int[] getData(String name)throws Exception{
        BufferedImage img = ImageIO.read(new File(name));
        BufferedImage slt = new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
        slt.getGraphics().drawImage(img,0,0,100,100,null);
        //ImageIO.write(slt,"jpeg",new File("slt.jpg"));
        int[] data = new int[256];
        for(int x = 0;x<slt.getWidth();x++){
            for(int y = 0;y<slt.getHeight();y++){
                int rgb = slt.getRGB(x,y);
                Color myColor = new Color(rgb);
                int r = myColor.getRed();
                int g = myColor.getGreen();
                int b = myColor.getBlue();
                data[(r+g+b)/3]++;
            }
        }
        //data 就是所谓图形学当中的直方图的概念
        return data;
    }
    public static float compare(int[] s,int[] t){
        float result = 0F;
        for(int i = 0;i<256;i++){
            int abs = Math.abs(s[i]-t[i]);
            int max = Math.max(s[i],t[i]);
            result += (1-((float)abs/(max==0?1:max)));
        }
        return (result/256)*100;
    }
    
}
