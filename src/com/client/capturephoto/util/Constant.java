package com.client.capturephoto.util;

import java.text.SimpleDateFormat;

public class Constant
{
    
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "yyyyMMddHHmmss");
    
    public static final boolean DEBUG = false;
    
    public static final String TEXT_CODE = "text_code";
    
    public static final String SCAN_RESULT = "scan_result";
    
    public static final String PICTURE_WIDTH = "picture_width";
    
    public static final String PICTURE_HEIGHT = "picture_height";
    
    public static final String USER_NAME = "user_name";
    
    public static final String PASSWORD = "password";
    
    public static final String URL = "url";
    
    public static final String TRANS = "trans";
    
    public static final String LIMIT = "limit";
    
    public static final String[] ARRAY_TRANS = { "原分辨率", "1 : 2 压缩", "1 : 3 压缩", "1 : 4 压缩" };
    
    public static final String[] ARRAY_LIMIT;
    
    public static final String[] ARRAY_NUMBER = { "2", "3", "4" };
    
    public static final String[] ARRAY_SIZE = { "50%", "60%", "70%", "80%", "90%"};
    
    static
    {
        ARRAY_LIMIT = new String[10];
        for (int i = 1; i <= ARRAY_LIMIT.length; i++)
        {
            ARRAY_LIMIT[i - 1] = ARRAY_LIMIT.length - i + 1 + "";
        }
    }
    
    public static final String NUMBER = "number";
    
    public static final String SIZE = "size";
    
    public static final int DEFAULT_TRANS_POSITION = 1;
    
    public static final int DEFAULT_LIMIT_POSITION = 0;
    
    public static final int DEFAULT_NUMBER_POSITION = 1;
    
    public static final int DEFAULT_SIZE_POSITION = 2;
    
    public static final int COUNT = 2;
    
    public static final int TAKE_BIG_PICTURE = 1;
    
    public static final int CROP_BIG_PICTURE = 2;
    
    public final static int SCANNIN_REQUEST_CODE = 5;
    
    public final static int TAKE_PICTURE_REQUEST_CODE = 6;
    
    private static String path = "/mnt/sdcard/11111/";
    
    private static String end = ".jpg";
    
    private static final String[] FILENAMES = { "dfsdfs",
            "P20818-160904", "P20818-160913", "P20818-162003", "P20818-162026",
            "P20818-162225", "P20818-162227", "P20818-162228", "P20818-162230",
            "P20818-162422", "P20818-162424", "P30208-160518", "P30208-160523",
            "P30208-160526", "P30208-160529", "P30209-110217", "P30227-212231",
            "P30428-123450", "P30429-180200", "P30429-180648", "P30429-180907",
            "P30429-182441", "P30429-182931", "P30501-145523", "P30501-145720",
            "P30501-160200", "P30501-160431", "P30501-160828", "P30501-160900",
            "P30501-161435", "P30501-161900", "P30501-162818", "P30504-110206",
            "P30504-110247", "P30504-111136", "P30504-111402", "P30504-113354",
            "P30504-113403", "P30504-113413", "P30504-114618", "P30504-114857",
            "P30504-115035", "P30504-120114", "P30504-120548", "P30504-121125",
            "P30504-121512", "P30504-121708", "P30504-122410", "P30504-122845",
            "P30504-124743", "P30504-124755", "P30504-130236", "P30504-130524",
            "P30504-132434", "P30504-132508", "P30504-132803", "P30504-133310",
            "P30504-133625", "P30504-133651", "P30504-133738", "P30504-133957",
            "P30504-134007", "P30504-134126", "P30504-134919", "P30504-134952",
            "P30504-134957", "P30504-135954", "P30504-135956" };
    
    public static final String[] uris = new String[FILENAMES.length];
    
    static
    {
        for (int i = 0; i < uris.length; i++)
        {
            uris[i] = path + FILENAMES[i] + end;
        }
    }
    
    /**
     * 数据库
     */
    public static final String DB_NAME = "pictures";
    
    public static final int VERSION = 1;
    
    public static final String TABLE_NAME = DB_NAME;
    
    public static class Column
    {
        public static final String TIME = "time";
        
        public static final String FULL_NAME = "full_name";
        
        public static final String IMAGE_CODE = "image_code";
        
        public static final String NOT_USED = "not_used";
        
        public static final String NOT_USED_1 = "not_used_1";
        
        public static final String NOT_USED_2 = "not_used_2";
    }
    
}
