package util;

import java.awt.*;
import java.util.Map;

public class Constants {
    public static final String appName = "Quản Lý Điểm Danh";
    public static final String appFontName = "Segoe UI";
    public static final Font appFont = new Font(appFontName, Font.PLAIN, 22);

    public static final Insets fieldMargin = new Insets(5, 5 ,5, 5);

    public static final int maxWeekIndex = 15;

    public static void setFontForAll() {
        for (Map.Entry<Object, Object> entry : javax.swing.UIManager.getDefaults().entrySet()) {
            Object key = entry.getKey();
            Object value = javax.swing.UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                javax.swing.UIManager.put(key, Constants.appFont);
            }
        }
    }
}
