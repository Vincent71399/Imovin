package sg.edu.nus.imovin.System;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.imovin.Retrofit.Object.LibraryData;

public class LibraryURLConstants {
    public static final String LINK1_TITLE = "Getting closer to understanding how exercise keeps brains young";
    public static final String LINK1_PICTURE_URL = "https://hhp-blog.s3.amazonaws.com/2015/09/seniors-exercising-jogging-300x200.jpg";
    public static final String LINK1_URL = "https://www.health.harvard.edu/blog/getting-closer-to-understanding-how-exercise-keeps-brains-young-201509048246";
    public static final String LINK1_BLOG = "Harvard Health Blog";
    public static final String LINK1_PUBLIC_YEAR = "2015";

    public static final String LINK2_TITLE = "Regular exercise changes the brain to improve memory, thinking skills";
    public static final String LINK2_PICTURE_URL = "https://hhp-blog.s3.amazonaws.com/2018/03/Woman-Runner-Tying-Sport-Shoes-52582435-300x200.jpg";
    public static final String LINK2_URL = "https://www.health.harvard.edu/blog/regular-exercise-changes-brain-improve-memory-thinking-skills-201404097110";
    public static final String LINK2_BLOG = "Harvard Health Blog";
    public static final String LINK2_PUBLIC_YEAR = "2018";

    public static final String LINK3_TITLE = "What kinds of exercise are good for brain health?";
    public static final String LINK3_PICTURE_URL = "https://hhp-blog.s3.amazonaws.com/2018/05/iStock-882232456-300x403.jpg";
    public static final String LINK3_URL = "https://www.health.harvard.edu/blog/what-kinds-of-exercise-are-good-for-brain-health-2018050213762";
    public static final String LINK3_BLOG = "Harvard Health Blog";
    public static final String LINK3_PUBLIC_YEAR = "2015";

    public static final String LINK4_TITLE = "Fitness May Help in Managing Diabetes, Even in Obese Individuals";
    public static final String LINK4_PICTURE_URL = "https://newsatjama.files.wordpress.com/2012/06/06-15-12-exercise-prescription-istock_000011598372xsmall.jpg";
    public static final String LINK4_URL = "https://newsatjama.jama.com/2012/06/15/fitness-may-help-in-managing-diabetes-even-in-obese-individuals/";
    public static final String LINK4_BLOG = "news@JAMA";
    public static final String LINK4_PUBLIC_YEAR = "2012";

    public static final String LINK5_TITLE = "This just in: Exercise is good for you";
    public static final String LINK5_PICTURE_URL = "https://hhp-blog.s3.amazonaws.com/2017/07/iStock-626123946-Copy-300x200.jpg";
    public static final String LINK5_URL = "https://www.health.harvard.edu/blog/this-just-in-exercise-is-good-for-you-2017072012004";
    public static final String LINK5_BLOG = "Harvard Health Blog";
    public static final String LINK5_PUBLIC_YEAR = "2016";

    public static List<LibraryData> getLibraryList(){
        List<LibraryData> libraryDataList = new ArrayList<>();
        libraryDataList.add(new LibraryData(LINK1_TITLE, LINK1_BLOG, LINK1_PUBLIC_YEAR, LINK1_PICTURE_URL, LINK1_URL));
        libraryDataList.add(new LibraryData(LINK2_TITLE, LINK2_BLOG, LINK2_PUBLIC_YEAR, LINK2_PICTURE_URL, LINK2_URL));
        libraryDataList.add(new LibraryData(LINK3_TITLE, LINK3_BLOG, LINK3_PUBLIC_YEAR, LINK3_PICTURE_URL, LINK3_URL));
        libraryDataList.add(new LibraryData(LINK4_TITLE, LINK4_BLOG, LINK4_PUBLIC_YEAR, LINK4_PICTURE_URL, LINK4_URL));
        libraryDataList.add(new LibraryData(LINK5_TITLE, LINK5_BLOG, LINK5_PUBLIC_YEAR, LINK5_PICTURE_URL, LINK5_URL));

        return libraryDataList;
    }
}
