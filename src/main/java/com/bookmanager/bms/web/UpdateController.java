package com.bookmanager.bms.web;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/update")
public class UpdateController {

    /**
     * 我的上傳方法
     * @param req
     * @param dirName
     * @return
     */
    private String myUpdate(HttpServletRequest req, String dirName) {
        String res = null;  // 返回網路路徑
        try {
            String staticDir = ResourceUtils.getURL("classpath:").getPath() + "static";  // 得到classes/static目錄
            String localDir = staticDir + "/" + dirName;   //本地目錄
            // 如果結果目錄不存在，則創建目錄
            File resDirFile = new File(localDir);
            if(!resDirFile.exists()) {
                boolean flag = resDirFile.mkdirs();
                if(!flag) throw new RuntimeException("創建結果目錄失敗");
            }
            //先判斷上傳的數據是否多段數據（只有是多段的數據，才是文件上傳的）
            if (ServletFileUpload.isMultipartContent(req)) {
                // 創建 FileItemFactory 工廠實現類
                FileItemFactory fileItemFactory = new DiskFileItemFactory();
                // 創建用於解析上傳數據的工具類 ServletFileUpload 類
                ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
                // 解析上傳的數據，得到每一個表單項 FileItem
                List<FileItem> list = servletFileUpload.parseRequest(new ServletRequestContext(req));
                // 循環判斷，每一個表單項，是普通類型，還是上傳的文件
                for (FileItem fileItem : list) {
                    if ( !fileItem.isFormField()) { // 是上傳的文件
                        // 上傳的文件
                        System.out.println("表單項的 name 屬性值：" + fileItem.getFieldName());
                        System.out.println("上傳的檔案名：" + fileItem.getName());
                        // 加個時間戳防止重名
                        String newFileName = System.currentTimeMillis() + fileItem.getName();
                        // 寫文件
                        File file = new File(localDir + "/" + newFileName);
                        fileItem.write(file);
                        // 返回值
                        res = "http://localhost:8092/BookManager/" + dirName + "/" + newFileName;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 上傳圖片
     * @param req
     * @return
     */
    @RequestMapping("/updateImg")
    @ResponseBody
    public Map<String,Object> updateImg(HttpServletRequest req){
        String resPath = myUpdate(req, "pictures");

        Map<String,Object> res = new HashMap<>();
        res.put("code",0);
        res.put("data", resPath);

        return res;
    }

}
