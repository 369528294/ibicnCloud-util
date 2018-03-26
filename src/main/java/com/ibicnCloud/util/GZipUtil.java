package com.ibicnCloud.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.ibicnCloud.util.base64.BASE64Decoder;
import com.ibicnCloud.util.base64.BASE64Encoder;

/**
 * GZIP工具
 *
 * @since 1.0
 */
public abstract class GZipUtil {

    public static final int BUFFER = 1024;
    public static final String EXT = ".gz";

    /**
     * 压缩数据
     *
     * @param input
     * @return
     */
    public static String yasuo(String str) {
        byte[] data = new byte[1];
        try {
            byte[] input = str.getBytes("utf-8");
            data = GZipUtil.compress(input);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return new BASE64Encoder().encodeBuffer(data);

    }

    /**
     * 解压缩数据
     *
     * @param input
     * @return
     */
    public static String jieyasuo(String str) {
        byte[] output = new byte[1];
        try {
            byte[] input = new BASE64Decoder().decodeBuffer(str);
            output = GZipUtil.decompress(input);
            return new String(output, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 数据压缩
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] compress(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 压缩
        compress(bais, baos);

        byte[] output = baos.toByteArray();

        baos.flush();
        baos.close();

        bais.close();

        return output;
    }

    /**
     * 文件压缩
     *
     * @param file
     * @throws Exception
     */
    public static void compress(File file) throws Exception {
        compress(file, true);
    }

    /**
     * 文件压缩
     *
     * @param file
     * @param delete
     *            是否删除原始文件
     * @throws Exception
     */
    public static void compress(File file, boolean delete) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(file.getPath() + EXT);

        compress(fis, fos);

        fis.close();
        fos.flush();
        fos.close();

        if (delete) {
            file.delete();
        }
    }

    /**
     * 数据压缩
     *
     * @param is
     * @param os
     * @throws Exception
     */
    public static void compress(InputStream is, OutputStream os) throws Exception {

        GZIPOutputStream gos = new GZIPOutputStream(os);

        int count;
        byte data[] = new byte[BUFFER];
        while ((count = is.read(data, 0, BUFFER)) != -1) {
            gos.write(data, 0, count);
        }

        gos.finish();

        gos.flush();
        gos.close();
    }

    /**
     * 文件压缩
     *
     * @param path
     * @throws Exception
     */
    public static void compress(String path) throws Exception {
        compress(path, true);
    }

    /**
     * 文件压缩
     *
     * @param path
     * @param delete
     *            是否删除原始文件
     * @throws Exception
     */
    public static void compress(String path, boolean delete) throws Exception {
        File file = new File(path);
        compress(file, delete);
    }

    /**
     * 数据解压缩
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] decompress(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 解压缩

        decompress(bais, baos);

        data = baos.toByteArray();

        baos.flush();
        baos.close();

        bais.close();

        return data;
    }

    /**
     * 文件解压缩
     *
     * @param file
     * @throws Exception
     */
    public static void decompress(File file) throws Exception {
        decompress(file, true);
    }

    /**
     * 文件解压缩
     *
     * @param file
     * @param delete
     *            是否删除原始文件
     * @throws Exception
     */
    public static void decompress(File file, boolean delete) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(file.getPath().replace(EXT, ""));
        decompress(fis, fos);
        fis.close();
        fos.flush();
        fos.close();

        if (delete) {
            file.delete();
        }
    }

    /**
     * 数据解压缩
     *
     * @param is
     * @param os
     * @throws Exception
     */
    public static void decompress(InputStream is, OutputStream os) throws Exception {

        GZIPInputStream gis = new GZIPInputStream(is);

        int count;
        byte data[] = new byte[BUFFER];
        while ((count = gis.read(data, 0, BUFFER)) != -1) {
            os.write(data, 0, count);
        }

        gis.close();
    }

    /**
     * 文件解压缩
     *
     * @param path
     * @throws Exception
     */
    public static void decompress(String path) throws Exception {
        decompress(path, true);
    }

    /**
     * 文件解压缩
     *
     * @param path
     * @param delete
     *            是否删除原始文件
     * @throws Exception
     */
    public static void decompress(String path, boolean delete) throws Exception {
        File file = new File(path);
        decompress(file, delete);
    }

    public static void main(String[] args) {
        String a = "{\"data\":{\"companyid\":2,\"lastModifyTime\":\"2014-12-26 11:20:46\",\"synchroDepartments\":[{\"departmentName\":\"一部\",\"department_id\":1,\"parentDepartmentStr\":\"#1,#404,\",\"parentDepartment_id\":\"404\",\"sortID\":0},{\"departmentName\":\"四部\",\"department_id\":49,\"parentDepartmentStr\":\"#49,#402,\",\"parentDepartment_id\":\"402\",\"sortID\":0},{\"departmentName\":\"五部\",\"department_id\":80,\"parentDepartmentStr\":\"#80,#403,\",\"parentDepartment_id\":\"403\",\"sortID\":0},{\"departmentName\":\"十部一处\",\"department_id\":3,\"parentDepartmentStr\":\"#3,#2,\",\"parentDepartment_id\":\"2\",\"sortID\":1},{\"departmentName\":\"一部一处\",\"department_id\":13,\"parentDepartmentStr\":\"#13,#1,\",\"parentDepartment_id\":\"1\",\"sortID\":1},{\"departmentName\":\"二部一处\",\"department_id\":20,\"parentDepartmentStr\":\"#20,#19,\",\"parentDepartment_id\":\"19\",\"sortID\":1},{\"departmentName\":\"二部二处\",\"department_id\":24,\"parentDepartmentStr\":\"#24,#19,\",\"parentDepartment_id\":\"19\",\"sortID\":1},{\"departmentName\":\"二部三处\",\"department_id\":28,\"parentDepartmentStr\":\"#28,#19,\",\"parentDepartment_id\":\"19\",\"sortID\":1},{\"departmentName\":\"二部四处\",\"department_id\":31,\"parentDepartmentStr\":\"#31,#19,\",\"parentDepartment_id\":\"19\",\"sortID\":1},{\"departmentName\":\"二部五处\",\"department_id\":33,\"parentDepartmentStr\":\"#33,#19,\",\"parentDepartment_id\":\"19\",\"sortID\":1},{\"departmentName\":\"四部一处\",\"department_id\":50,\"parentDepartmentStr\":\"#50,#49,\",\"parentDepartment_id\":\"49\",\"sortID\":1},{\"departmentName\":\"九部一处\",\"department_id\":61,\"parentDepartmentStr\":\"#61,#60,\",\"parentDepartment_id\":\"60\",\"sortID\":1},{\"departmentName\":\"五一部一处\",\"department_id\":87,\"parentDepartmentStr\":\"#87,#81,\",\"parentDepartment_id\":\"81\",\"sortID\":1},{\"departmentName\":\"五部二处\",\"department_id\":88,\"parentDepartmentStr\":\"#88,#80,\",\"parentDepartment_id\":\"80\",\"sortID\":1},{\"departmentName\":\"五部三处\",\"department_id\":89,\"parentDepartmentStr\":\"#89,#80,\",\"parentDepartment_id\":\"80\",\"sortID\":1},{\"departmentName\":\"五部四处\",\"department_id\":90,\"parentDepartmentStr\":\"#90,#80,\",\"parentDepartment_id\":\"80\",\"sortID\":1},{\"departmentName\":\"五部五处\",\"department_id\":91,\"parentDepartmentStr\":\"#91,#80,\",\"parentDepartment_id\":\"80\",\"sortID\":1},{\"departmentName\":\"五部六处\",\"department_id\":92,\"parentDepartmentStr\":\"#92,#80,\",\"parentDepartment_id\":\"80\",\"sortID\":1},{\"departmentName\":\"五部七处\",\"department_id\":93,\"parentDepartmentStr\":\"#93,#80,\",\"parentDepartment_id\":\"80\",\"sortID\":1},{\"departmentName\":\"五部八处\",\"department_id\":94,\"parentDepartmentStr\":\"#94,#80,\",\"parentDepartment_id\":\"80\",\"sortID\":1},{\"departmentName\":\"五部九处\",\"department_id\":95,\"parentDepartmentStr\":\"#95,#80,\",\"parentDepartment_id\":\"80\",\"sortID\":1},{\"departmentName\":\"五部十处\",\"department_id\":96,\"parentDepartmentStr\":\"#96,#80,\",\"parentDepartment_id\":\"80\",\"sortID\":1},{\"departmentName\":\"五部十一处\",\"department_id\":97,\"parentDepartmentStr\":\"#97,#80,\",\"parentDepartment_id\":\"80\",\"sortID\":1},{\"departmentName\":\"二部六处\",\"department_id\":190,\"parentDepartmentStr\":\"#190,#19,\",\"parentDepartment_id\":\"19\",\"sortID\":1},{\"departmentName\":\"网络事业部-全网营销\",\"department_id\":288,\"parentDepartmentStr\":\"#288,#165,\",\"parentDepartment_id\":\"165\",\"sortID\":1},{\"departmentName\":\"商学院三纵一处\",\"department_id\":352,\"parentDepartmentStr\":\"#352,#370,\",\"parentDepartment_id\":\"370\",\"sortID\":1},{\"departmentName\":\"客服部\",\"department_id\":354,\"parentDepartmentStr\":\"#354,#159,\",\"parentDepartment_id\":\"159\",\"sortID\":1},{\"departmentName\":\"商学院三纵三处\",\"department_id\":359,\"parentDepartmentStr\":\"#359,#370,\",\"parentDepartment_id\":\"370\",\"sortID\":1},{\"departmentName\":\"商学院三纵二处\",\"department_id\":360,\"parentDepartmentStr\":\"#360,#370,\",\"parentDepartment_id\":\"370\",\"sortID\":1},{\"departmentName\":\"商学院一纵八处\",\"department_id\":376,\"parentDepartmentStr\":\"#376,#369,\",\"parentDepartment_id\":\"369\",\"sortID\":1},{\"departmentName\":\"商学院一纵二处\",\"department_id\":377,\"parentDepartmentStr\":\"#377,#369,\",\"parentDepartment_id\":\"369\",\"sortID\":1},{\"departmentName\":\"商学院一纵九处\",\"department_id\":378,\"parentDepartmentStr\":\"#378,#369,\",\"parentDepartment_id\":\"369\",\"sortID\":1},{\"departmentName\":\"商学院一纵七处\",\"department_id\":379,\"parentDepartmentStr\":\"#379,#369,\",\"parentDepartment_id\":\"369\",\"sortID\":1},{\"departmentName\":\"商学院一纵三处\",\"department_id\":380,\"parentDepartmentStr\":\"#380,#369,\",\"parentDepartment_id\":\"369\",\"sortID\":1},{\"departmentName\":\"商学院一纵十处\",\"department_id\":381,\"parentDepartmentStr\":\"#381,#369,\",\"parentDepartment_id\":\"369\",\"sortID\":1},{\"departmentName\":\"商学院一纵四处\",\"department_id\":382,\"parentDepartmentStr\":\"#382,#369,\",\"parentDepartment_id\":\"369\",\"sortID\":1},{\"departmentName\":\"商学院一纵五处\",\"department_id\":383,\"parentDepartmentStr\":\"#383,#369,\",\"parentDepartment_id\":\"369\",\"sortID\":1},{\"departmentName\":\"商学院一纵一处\",\"department_id\":384,\"parentDepartmentStr\":\"#384,#369,\",\"parentDepartment_id\":\"369\",\"sortID\":1},{\"departmentName\":\"国联公司\",\"department_id\":397,\"parentDepartmentStr\":\"#397,\",\"parentDepartment_id\":\"\",\"sortID\":1},{\"departmentName\":\"十部二处\",\"department_id\":11,\"parentDepartmentStr\":\"#11,#2,\",\"parentDepartment_id\":\"2\",\"sortID\":2},{\"departmentName\":\"一部二处\",\"department_id\":14,\"parentDepartmentStr\":\"#14,#1,\",\"parentDepartment_id\":\"1\",\"sortID\":2},{\"departmentName\":\"四部二处\",\"department_id\":57,\"parentDepartmentStr\":\"#57,#49,\",\"parentDepartment_id\":\"49\",\"sortID\":2},{\"departmentName\":\"九部二处\",\"department_id\":65,\"parentDepartmentStr\":\"#65,#60,\",\"parentDepartment_id\":\"60\",\"sortID\":2},{\"departmentName\":\"五一部\",\"department_id\":81,\"parentDepartmentStr\":\"#81,#403,\",\"parentDepartment_id\":\"403\",\"sortID\":2},{\"departmentName\":\"五一部二处\",\"department_id\":83,\"parentDepartmentStr\":\"#83,#81,\",\"parentDepartment_id\":\"81\",\"sortID\":2},{\"departmentName\":\"网络事业部-设计部\",\"department_id\":351,\"parentDepartmentStr\":\"#351,#165,\",\"parentDepartment_id\":\"165\",\"sortID\":2},{\"departmentName\":\"一部三处\",\"department_id\":15,\"parentDepartmentStr\":\"#15,#1,\",\"parentDepartment_id\":\"1\",\"sortID\":3},{\"departmentName\":\"九部三处\",\"department_id\":62,\"parentDepartmentStr\":\"#62,#60,\",\"parentDepartment_id\":\"60\",\"sortID\":3},{\"departmentName\":\"五一部三处\",\"department_id\":84,\"parentDepartmentStr\":\"#84,#81,\",\"parentDepartment_id\":\"81\",\"sortID\":3},{\"departmentName\":\"十部三处\",\"department_id\":175,\"parentDepartmentStr\":\"#175,#2,\",\"parentDepartment_id\":\"2\",\"sortID\":3},{\"departmentName\":\"网络事业部-运营部\",\"department_id\":357,\"parentDepartmentStr\":\"#357,#165,\",\"parentDepartment_id\":\"165\",\"sortID\":3},{\"departmentName\":\"十部四处\",\"department_id\":6,\"parentDepartmentStr\":\"#6,#2,\",\"parentDepartment_id\":\"2\",\"sortID\":4},{\"departmentName\":\"一部四处\",\"department_id\":16,\"parentDepartmentStr\":\"#16,#1,\",\"parentDepartment_id\":\"1\",\"sortID\":4},{\"departmentName\":\"九部四处\",\"department_id\":63,\"parentDepartmentStr\":\"#63,#60,\",\"parentDepartment_id\":\"60\",\"sortID\":4},{\"departmentName\":\"四部四处\",\"department_id\":67,\"parentDepartmentStr\":\"#67,#49,\",\"parentDepartment_id\":\"49\",\"sortID\":4},{\"departmentName\":\"五一部四处\",\"department_id\":85,\"parentDepartmentStr\":\"#85,#81,\",\"parentDepartment_id\":\"81\",\"sortID\":4},{\"departmentName\":\"网络事业部-产品部\",\"department_id\":353,\"parentDepartmentStr\":\"#353,#165,\",\"parentDepartment_id\":\"165\",\"sortID\":4},{\"departmentName\":\"四部五处\",\"department_id\":68,\"parentDepartmentStr\":\"#68,#49,\",\"parentDepartment_id\":\"49\",\"sortID\":5},{\"departmentName\":\"五一部五处\",\"department_id\":86,\"parentDepartmentStr\":\"#86,#81,\",\"parentDepartment_id\":\"81\",\"sortID\":5},{\"departmentName\":\"九部五处\",\"department_id\":156,\"parentDepartmentStr\":\"#156,#60,\",\"parentDepartment_id\":\"60\",\"sortID\":5},{\"departmentName\":\"一部五处\",\"department_id\":184,\"parentDepartmentStr\":\"#184,#1,\",\"parentDepartment_id\":\"1\",\"sortID\":5},{\"departmentName\":\"网络事业部-技术一部\",\"department_id\":350,\"parentDepartmentStr\":\"#350,#165,\",\"parentDepartment_id\":\"165\",\"sortID\":5},{\"departmentName\":\"董秘办\",\"department_id\":400,\"parentDepartmentStr\":\"#400,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":5},{\"departmentName\":\"九部六处\",\"department_id\":64,\"parentDepartmentStr\":\"#64,#60,\",\"parentDepartment_id\":\"60\",\"sortID\":6},{\"departmentName\":\"四部六处\",\"department_id\":69,\"parentDepartmentStr\":\"#69,#49,\",\"parentDepartment_id\":\"49\",\"sortID\":6},{\"departmentName\":\"五一部六处\",\"department_id\":82,\"parentDepartmentStr\":\"#82,#81,\",\"parentDepartment_id\":\"81\",\"sortID\":6},{\"departmentName\":\"十部六处\",\"department_id\":205,\"parentDepartmentStr\":\"#205,#2,\",\"parentDepartment_id\":\"2\",\"sortID\":6},{\"departmentName\":\"一部六处\",\"department_id\":251,\"parentDepartmentStr\":\"#251,#1,\",\"parentDepartment_id\":\"1\",\"sortID\":6},{\"departmentName\":\"网络事业部-技术二部\",\"department_id\":291,\"parentDepartmentStr\":\"#291,#165,\",\"parentDepartment_id\":\"165\",\"sortID\":6},{\"departmentName\":\"四部七处\",\"department_id\":72,\"parentDepartmentStr\":\"#72,#49,\",\"parentDepartment_id\":\"49\",\"sortID\":7},{\"departmentName\":\"一部七处\",\"department_id\":227,\"parentDepartmentStr\":\"#227,#1,\",\"parentDepartment_id\":\"1\",\"sortID\":7},{\"departmentName\":\"九部七处\",\"department_id\":261,\"parentDepartmentStr\":\"#261,#60,\",\"parentDepartment_id\":\"60\",\"sortID\":7},{\"departmentName\":\"十部七处\",\"department_id\":343,\"parentDepartmentStr\":\"#343,#2,\",\"parentDepartment_id\":\"2\",\"sortID\":7},{\"departmentName\":\"四部八处\",\"department_id\":77,\"parentDepartmentStr\":\"#77,#49,\",\"parentDepartment_id\":\"49\",\"sortID\":8},{\"departmentName\":\"九部八处\",\"department_id\":180,\"parentDepartmentStr\":\"#180,#60,\",\"parentDepartment_id\":\"60\",\"sortID\":8},{\"departmentName\":\"十部八处\",\"department_id\":230,\"parentDepartmentStr\":\"#230,#2,\",\"parentDepartment_id\":\"2\",\"sortID\":8},{\"departmentName\":\"一部八处\",\"department_id\":305,\"parentDepartmentStr\":\"#305,#1,\",\"parentDepartment_id\":\"1\",\"sortID\":8},{\"departmentName\":\"四部九处\",\"department_id\":78,\"parentDepartmentStr\":\"#78,#49,\",\"parentDepartment_id\":\"49\",\"sortID\":9},{\"departmentName\":\"十部九处\",\"department_id\":147,\"parentDepartmentStr\":\"#147,#2,\",\"parentDepartment_id\":\"2\",\"sortID\":9},{\"departmentName\":\"九部九处\",\"department_id\":240,\"parentDepartmentStr\":\"#240,#60,\",\"parentDepartment_id\":\"60\",\"sortID\":9},{\"departmentName\":\"一部九处\",\"department_id\":317,\"parentDepartmentStr\":\"#317,#1,\",\"parentDepartment_id\":\"1\",\"sortID\":9},{\"departmentName\":\"十部\",\"department_id\":2,\"parentDepartmentStr\":\"#2,#404,\",\"parentDepartment_id\":\"404\",\"sortID\":10},{\"departmentName\":\"九部\",\"department_id\":60,\"parentDepartmentStr\":\"#60,#402,\",\"parentDepartment_id\":\"402\",\"sortID\":10},{\"departmentName\":\"四部十处\",\"department_id\":79,\"parentDepartmentStr\":\"#79,#49,\",\"parentDepartment_id\":\"49\",\"sortID\":10},{\"departmentName\":\"十部十处\",\"department_id\":143,\"parentDepartmentStr\":\"#143,#2,\",\"parentDepartment_id\":\"2\",\"sortID\":10},{\"departmentName\":\"总裁助理办公室\",\"department_id\":159,\"parentDepartmentStr\":\"#159,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":10},{\"departmentName\":\"九部十处\",\"department_id\":260,\"parentDepartmentStr\":\"#260,#60,\",\"parentDepartment_id\":\"60\",\"sortID\":10},{\"departmentName\":\"一部十处\",\"department_id\":328,\"parentDepartmentStr\":\"#328,#1,\",\"parentDepartment_id\":\"1\",\"sortID\":10},{\"departmentName\":\"商学院一纵\",\"department_id\":369,\"parentDepartmentStr\":\"#369,#405,\",\"parentDepartment_id\":\"405\",\"sortID\":10},{\"departmentName\":\"财务部\",\"department_id\":149,\"parentDepartmentStr\":\"#149,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":20},{\"departmentName\":\"商学院二纵\",\"department_id\":406,\"parentDepartmentStr\":\"#406,#405,\",\"parentDepartment_id\":\"405\",\"sortID\":20},{\"departmentName\":\"网络事业部\",\"department_id\":165,\"parentDepartmentStr\":\"#165,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":30},{\"departmentName\":\"商学院三纵\",\"department_id\":370,\"parentDepartmentStr\":\"#370,#405,\",\"parentDepartment_id\":\"405\",\"sortID\":30},{\"departmentName\":\"人力资源部\",\"department_id\":148,\"parentDepartmentStr\":\"#148,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":40},{\"departmentName\":\"行政保障部\",\"department_id\":243,\"parentDepartmentStr\":\"#243,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":50},{\"departmentName\":\"图文设计制作部\",\"department_id\":399,\"parentDepartmentStr\":\"#399,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":60},{\"departmentName\":\"资源合作和策划中心\",\"department_id\":166,\"parentDepartmentStr\":\"#166,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":70},{\"departmentName\":\"印务办\",\"department_id\":139,\"parentDepartmentStr\":\"#139,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":80},{\"departmentName\":\"金融平台事业部\",\"department_id\":333,\"parentDepartmentStr\":\"#333,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":90},{\"departmentName\":\"A群\",\"department_id\":402,\"parentDepartmentStr\":\"#402,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":500},{\"departmentName\":\"B群\",\"department_id\":404,\"parentDepartmentStr\":\"#404,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":550},{\"departmentName\":\"C群\",\"department_id\":403,\"parentDepartmentStr\":\"#403,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":600},{\"departmentName\":\"国联商学院\",\"department_id\":405,\"parentDepartmentStr\":\"#405,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":700},{\"departmentName\":\"二部\",\"department_id\":19,\"parentDepartmentStr\":\"#19,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":2000},{\"departmentName\":\"三部一处\",\"department_id\":34,\"parentDepartmentStr\":\"#34,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":3100},{\"departmentName\":\"三二部\",\"department_id\":38,\"parentDepartmentStr\":\"#38,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":3200},{\"departmentName\":\"三部三处\",\"department_id\":43,\"parentDepartmentStr\":\"#43,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":3300},{\"departmentName\":\"三四部\",\"department_id\":44,\"parentDepartmentStr\":\"#44,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":3400},{\"departmentName\":\"六部一处\",\"department_id\":98,\"parentDepartmentStr\":\"#98,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":6100},{\"departmentName\":\"六部二处\",\"department_id\":108,\"parentDepartmentStr\":\"#108,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":6200},{\"departmentName\":\"六三部\",\"department_id\":111,\"parentDepartmentStr\":\"#111,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":6300},{\"departmentName\":\"六四部\",\"department_id\":120,\"parentDepartmentStr\":\"#120,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":6400},{\"departmentName\":\"七部一处\",\"department_id\":407,\"parentDepartmentStr\":\"#407,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":7100},{\"departmentName\":\"七部三处\",\"department_id\":408,\"parentDepartmentStr\":\"#408,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":7300},{\"departmentName\":\"八部一处\",\"department_id\":136,\"parentDepartmentStr\":\"#136,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":8100},{\"departmentName\":\"八部二处\",\"department_id\":137,\"parentDepartmentStr\":\"#137,#397,\",\"parentDepartment_id\":\"397\",\"sortID\":8200}]},\"errmsg\":\"\",\"status\":1}";
        System.out.println(a.length());
        String yasuoa = yasuo(a);
        System.out.println(yasuoa.length());
        String jiaya = jieyasuo(yasuoa);
        System.out.println(jiaya);
    }

}
