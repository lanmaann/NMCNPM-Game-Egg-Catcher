package util;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * =========================================================
 * IMAGE LOADER
 * =========================================================
 * lớp hỗ trợ load hình ảnh từ resource
 * 
 * chức năng:
 * - load image từ đường dẫn resource
 * - kiểm tra ảnh tồn tại
 * - trả về ảnh fallback khi lỗi
 * =========================================================
 */
public class ImageLoader {

    /**
     * load image từ resource path
     */
    public static Image load(String path) {

        // lấy URL từ resource
        URL url = ImageLoader.class.getResource(path);

        // =========================================================
        // IMAGE NOT FOUND
        // =========================================================

        if (url == null) {

            System.out.println("❌ Missing image: " + path);

            // trả về ảnh rỗng fallback
            return new ImageIcon(new byte[0]).getImage();
        }

        // =========================================================
        // LOAD IMAGE
        // =========================================================

        return new ImageIcon(url).getImage();
    }
}