package tool;
import java.io.File;
import java.util.ArrayList;


public class FileTool {
    public static ArrayList<String> refreshFileList(String strPath) 
    { 
        ArrayList<String> filelist = new ArrayList<String>(); 
        File dir = new File(strPath); 
        File[] files = dir.listFiles(); 
        if (files == null) 
        {
            return filelist; 
        }
        for (int i = 0; i < files.length; i++) 
        { 
            if (files[i].isDirectory()) 
            { 
                refreshFileList(files[i].getAbsolutePath()); 
            } 
            else
            { 
                files[i].getAbsolutePath().toLowerCase(); 
                //System.out.println(strFileName); 
                filelist.add(files[i].getAbsolutePath());					
            } 
        }
        return filelist;
    }
    
    public static void main(String[] args) 
    {
        // TODO Auto-generated method stub
        ArrayList<String>  test = refreshFileList("/home/linger/imdata/collar");
        System.out.println(test.get(668));
        System.out.println(test.get(669));
        System.out.println(test.get(670));
    }

}