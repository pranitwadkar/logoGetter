/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package logogetter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.imageio.*;


/**
 *
 * @author Pranit
 */
public class LogoGetter {
    
    public String text;

    public static void main(String[] args) {
        LogoGetter a= new LogoGetter();
        String s;
        Scanner scanner= new Scanner(System.in);
        System.out.println("Enter the name of a company");
        s= scanner.nextLine();
        a.logoGetter(s);
        System.out.println("Check the root folder to find the downloaded logo");
    }
    
    
    public void logoGetter(String name){
        String text=null;
        String LogoURL= "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+ name;
        
        try{
            URL logo= new URL(LogoURL);
            HttpURLConnection LogoConnection = (HttpURLConnection) logo.openConnection();
            Scanner scanner= new Scanner(LogoConnection.getInputStream());
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                text= text+line;
            }
            
        }
        catch(Exception e){
            System.err.println(e);
        }
        String Imagelink= text.substring((text.indexOf("unescapedUrl")+15), text.indexOf("\",", text.indexOf("unescapedUrl")+14 ));
        try{
            URL url= new URL(Imagelink);
            URLConnection urlConn= url.openConnection();
            urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            InputStream i = new  BufferedInputStream(urlConn.getInputStream());
            ByteArrayOutputStream o= new ByteArrayOutputStream();
            byte[] b= new byte[1024];
            int n=0;
            while(-1!=(n=i.read(b))){
                o.write(b,0,n);
            }
            i.close();
            o.close();
            byte[] response = o.toByteArray();
            
            FileOutputStream output= new FileOutputStream(name+".jpg");
            output.write(response);
            output.close();
        }
        catch(IOException e){
            System.err.println(e);
        }
        
        try{
            BufferedImage originalImage= ImageIO.read(new File(name+ ".jpg"));
            int type = originalImage.getType();
            
            //Insert Image dimensions here
            BufferedImage resizedImage = new BufferedImage(100, 100, type);
            
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, 100, 100, null);
            g.dispose();	
            g.setComposite(AlphaComposite.Src);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
            
            //Here is re-writes the original image (because the filename is the same)
            //Change filename if you want to keep the original image
            ImageIO.write(resizedImage, "jpg", new File(name+".jpg"));
        }
        catch(IOException e){
            System.err.println(e);
        }   
        
    }
    
}
