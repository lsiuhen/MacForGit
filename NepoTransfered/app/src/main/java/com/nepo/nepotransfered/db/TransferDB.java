package com.nepo.nepotransfered.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nepo.nepotransfered.TransferActivity;
import com.nepo.nepotransfered.app.Constant;
import com.nepo.nepotransfered.model.TransferModel;
import com.nepo.nepotransfered.model.TransferProgressModel;
import com.nepo.nepotransfered.model.TransferTypeModel;
import com.nepo.nepotransfered.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransferDB {
    /**
     * 表名(传输列表)
     */
    public static final String TBL_NAME = "transferTbl";
    /**
     * 表名(传输进程列表)
     */
    public static final String TBL_TRANSFERPROGRESSNAME = "transferProgressTbl";
    /**
     * 表名(传输类型)
     */
    public static final String TBL_TRANSFERTYPE_NAME = "transferTypeTbl";

    /**
     * 建表语句
     * 传输进程
     * 进行传输的队列
     * isTransfer 是否传输过 传输过为1，否则为0
     */
    public static final String CREATE_TBL_PROGRESS = "create table " + TBL_TRANSFERPROGRESSNAME +
            "(" + "type text," + "hashcode text," + "progress text," + "currentSize text," + "fileSize text," +"isTransfer text," + "transPath text" + ")";
    /**
     * 建表语句
     * 传输队列
     * isTransfer 是否传输过 传输过为1，否则为0
     * BLOB 保存为图片(音乐，视频缩略图)
     */
    public static final String CREATE_TBL = "create table " + TBL_NAME +
            "(" + "type text," + "hashcode text," + "progress text," +
            "currentSize text,"+ "fileSize text," + "transImg BLOB," + "isTransfer text," + "transPath text" + ")";

    /**
     * 建表语句
     * 传输类型
     */
    public static final String CREATE_TBL_TYPE = "create table " + TBL_TRANSFERTYPE_NAME + "("
            + "_id int," + "transferType text," + "transferTypeSize int," + "transferTypeResourceId int" + " )";


    private static TransferDB _TransferDB;

    private SQLiteDatabase db;

    private SQLDBHlper mDBHlper;

    public TransferDB(Context context) {
        mDBHlper = SQLDBHlper.getSQLDBHlper(context);
    }

    public static TransferDB getTransferDB(Context context) {
        if (_TransferDB == null) {
            _TransferDB = new TransferDB(context);
        }
        return _TransferDB;
    }

    /**
     * 插入ContentValues
     */
    private long insert(ContentValues values, String table) {
        db = mDBHlper.getWritableDatabase();
        try {
            long insertLine = db.insert(table, null, values);
            return insertLine;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * TODO TBL_TRANSFERTYPE_NAME [传输类型]表操作
     */
    /**
     * 添加默认精选集类型 (初始化数据)
     */
    public long inserDefaultType() {
        List<Integer> resourceId = Constant.getResourceId();
        String[] str = new String[6];
        str[0] = "音乐";
        str[1] = "视频";
        str[2] = "图片";
        str[3] = "离线地图包";
        str[4] = "车载升级包";
        str[5] = "车载应用";
        long line = 0;
        ContentValues values = new ContentValues();
        for (int i = 0; i < str.length; i++) {
            values.put("_id", (i + 1));
            values.put("transferType", str[i]);
            values.put("transferTypeSize", 0);
            values.put("transferTypeResourceId", resourceId.get(i));
            line = insert(values, TBL_TRANSFERTYPE_NAME);
        }
        return line;

    }

    /**
     * 更新传输类型中 各个传输列表的数量
     */
    private void upateTransferTypeSizeByType(String type) {
        int sizeByType = queryTransferSizeByType(type);
        db = mDBHlper.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put("transferTypeSize", sizeByType);
            int update = db.update(TBL_TRANSFERTYPE_NAME, values, "transferType = ?", new String[]{type});
            Utils.Sout("up", update);
        } catch (SQLException e) {
            Utils.Sout("upex", e);
            e.printStackTrace();
        }
    }

    /**
     * 清空传输类型表中数据
     */
    private void deletTransType() {
        db = mDBHlper.getWritableDatabase();
        try {
            int deleteLine = db.delete(TBL_TRANSFERTYPE_NAME, null, null);
            Utils.Sout("del", deleteLine);
        } catch (SQLException e) {
            Utils.Sout("delex", e);
            e.printStackTrace();
        }
    }

    /**
     * 插入拖动排序后的传输类型，
     *
     * @param transferTypeModels
     */
    public void insertTransTypeBySortList(List<TransferTypeModel> transferTypeModels) {
        deletTransType();
        long line = 0;
        ContentValues values = new ContentValues();
        for (int i = 0; i < transferTypeModels.size(); i++) {
            values.put("_id", transferTypeModels.get(i).getTypeId());
            values.put("transferType", transferTypeModels.get(i).getTypeName());
            values.put("transferTypeSize", transferTypeModels.get(i).getTransferListLength());
            values.put("transferTypeResourceId", transferTypeModels.get(i).getTransferBgResourceId());
            line = insert(values, TBL_TRANSFERTYPE_NAME);
        }
    }

    /**
     * 查询全部分类
     * 包含分类名称，具体分类数量
     */
    public List<TransferTypeModel> queryAllType() {
        List<TransferTypeModel> transferTypeModels = new ArrayList<>();
        db = mDBHlper.getReadableDatabase();
        Cursor cursor = db.query(TBL_TRANSFERTYPE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            TransferTypeModel transferTypeModel = new TransferTypeModel();
            transferTypeModel.setTypeName(cursor.getString(cursor.getColumnIndex("transferType")));
            transferTypeModel.setTransferListLength(cursor.getString(cursor.getColumnIndex("transferTypeSize")));
            transferTypeModel.setTransferBgResourceId(cursor.getInt(cursor.getColumnIndex("transferTypeResourceId")));
            transferTypeModel.setTypeId(cursor.getInt(cursor.getColumnIndex("_id")));
            transferTypeModels.add(transferTypeModel);
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return transferTypeModels;
    }


    /**
     * TODO TBL_NAME [传输队列]表操作
     */
    /**
     * 加入文件到传输列表
     *
     * @param
     */
    public long insertToTransfer(String type, TransferModel transferModel) {
        //获取当前类型的文件，判断表中是否存在
        List<TransferModel> valueListByKey = queryTransferByType(type);
        int flag = 0;
        long line = 0;
        for (int i = 0; i < valueListByKey.size(); i++) {
            if (valueListByKey.get(i).getTransHashCode().equals(transferModel.getTransHashCode())) {
                flag++;
            }
        }
        // 有重复Hashcode不添加
        if (flag == 0) {
            ContentValues values = new ContentValues();
            if (transferModel.getTransBitmap() != null) {

                ByteArrayOutputStream os = new ByteArrayOutputStream();
                /**
                 * Bitmap.CompressFormat.JPEG 和 Bitmap.CompressFormat.PNG
                 * JPEG 与 PNG 的是区别在于 JPEG是有损数据图像，PNG使用从LZ77派生的无损数据压缩算法。
                 * 这里建议使用PNG格式保存
                 * 100 表示的是质量为100%。当然，也可以改变成你所需要的百分比质量。
                 * os 是定义的字节输出流
                 *  .compress() 方法是将Bitmap压缩成指定格式和质量的输出流
                 */
                transferModel.getTransBitmap().compress(Bitmap.CompressFormat.PNG, 100, os);
                values.put("transImg", os.toByteArray());
            }
            values.put("type", type);
            values.put("hashcode", transferModel.getTransHashCode() + "");
            values.put("progress", transferModel.getTransProgress());
            values.put("currentSize", transferModel.getCurrentSize());
            values.put("fileSize", transferModel.getTransLength());
            values.put("isTransfer", "0");
            values.put("transPath", transferModel.getTransPath());
            line = insert(values, TBL_NAME);

            if (line > 0) {
                upateTransferTypeSizeByType(type);
            }

        }


        return line;

    }

    /**
     * 修改是否传输过的标志
     * 针对传输队列
     */
    public void updateIsTransed(TransferProgressModel model) {
        db = mDBHlper.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put("isTransfer", "1");
            values.put("progress", model.getTransProgress());
            values.put("currentSize", model.getCurrentSize());
            int update = db.update(TBL_NAME, values, "hashcode=?", new String[]{model.getTransHashCode()});
            // Utils.Sout("up", update);
            upateTransferTypeSizeByType(model.getTransType());
        } catch (SQLException e) {
            Utils.Sout("upex", e);
            e.printStackTrace();
        }
    }

    /**
     * 获取某一类型的传输列表，根据类型
     *
     * @param type
     * @return
     */
    public List<TransferModel> queryTransferByType(String type) {
        List<TransferModel> list = new ArrayList<>();
        db = mDBHlper.getReadableDatabase();
        try {
            String selection = "isTransfer=? and type=?";
            String[] selectionArgs = new String[]{"0", type};
            Cursor cursor = db.query(TBL_NAME, null, selection, selectionArgs, null, null, null);
            while (cursor.moveToNext()) {
                TransferModel model = new TransferModel();
                model.setTransPath(cursor.getString(cursor.getColumnIndex("transPath")));
                model.setTransHashCode(cursor.getString(cursor.getColumnIndex("hashcode")));
                model.setTransProgress(Integer.valueOf(cursor.getString(cursor.getColumnIndex("progress"))));
                model.setCurrentSize(cursor.getString(cursor.getColumnIndex("currentSize")));
                model.setTransType(cursor.getString(cursor.getColumnIndex("type")));
                model.setIsTransfered(cursor.getString(cursor.getColumnIndex("isTransfer")));

                /**得到Bitmap字节数据**/
                byte[] in = cursor.getBlob(cursor.getColumnIndex("transImg"));
                if (in != null) {
                    /**
                     * 根据Bitmap字节数据转换成 Bitmap对象
                     * BitmapFactory.decodeByteArray() 方法对字节数据，从0到字节的长进行解码，生成Bitmap对像。
                     **/
                    Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
                    model.setTransBitmap(bmpout);
                }


                list.add(model);
            }
            if (cursor != null && !cursor.isClosed())
                cursor.close();
//
        } catch (Exception e) {
            Utils.Sout("qeex", e);
        }

        return list;
    }

    /**
     * 根据类型查询出待传输的集合，以备添加今传输进程中
     */
    private List<TransferModel> queryTransferToProgressByType(String type) {
        List<TransferModel> list = new ArrayList<>();
        db = mDBHlper.getReadableDatabase();
        try {
            String selection = "isTransfer=? and type=?";
            String[] selectionArgs = new String[]{"0", type};
            Cursor cursor = db.query(TBL_NAME, null, selection, selectionArgs, null, null, null);
            while (cursor.moveToNext()) {
                TransferModel model = new TransferModel();
                model.setTransPath(cursor.getString(cursor.getColumnIndex("transPath")));
                model.setTransHashCode(cursor.getString(cursor.getColumnIndex("hashcode")));
                model.setTransProgress(Integer.valueOf(cursor.getString(cursor.getColumnIndex("progress"))));
                model.setCurrentSize(cursor.getString(cursor.getColumnIndex("currentSize")));
                model.setTransType(cursor.getString(cursor.getColumnIndex("type")));
                model.setIsTransfered(cursor.getString(cursor.getColumnIndex("isTransfer")));
                list.add(model);
            }
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        } catch (Exception e) {
            Utils.Sout("qeex", e);
        }

        return list;
    }

    /**
     * 获取传输完成相关的列表 根据类型
     */
    public List<TransferModel> querydExistTransedAll(String type) {
        List<TransferModel> list = new ArrayList<>();
        db = mDBHlper.getReadableDatabase();
        try {
            String selection = "isTransfer=? and type=?";
            String[] selectionArgs = new String[]{"1", type};
            Cursor cursor = db.query(TBL_NAME, null, selection, selectionArgs, null, null, null);
            while (cursor.moveToNext()) {
                TransferModel model = new TransferModel();
                model.setTransPath(cursor.getString(cursor.getColumnIndex("transPath")));
                model.setTransHashCode(cursor.getString(cursor.getColumnIndex("hashcode")));
                model.setTransProgress(Integer.valueOf(cursor.getString(cursor.getColumnIndex("progress"))));
                model.setCurrentSize(cursor.getString(cursor.getColumnIndex("currentSize")));
                model.setTransType(cursor.getString(cursor.getColumnIndex("type")));
                model.setIsTransfered(cursor.getString(cursor.getColumnIndex("isTransfer")));
                list.add(model);
            }
            if (cursor != null && !cursor.isClosed())
                cursor.close();
//
        } catch (Exception e) {
            Utils.Sout("qeex", e);
        }

        return list;


    }

    /**
     * 获取未传输相关的列表
     */
    public List<TransferModel> queryNoTranserAll() {
        List<TransferModel> list = new ArrayList<>();
        db = mDBHlper.getReadableDatabase();
        try {
            String selection = "isTransfer=?";
            String[] selectionArgs = new String[]{"0"};
            Cursor cursor = db.query(TBL_NAME, null, selection, selectionArgs, null, null, null);
            while (cursor.moveToNext()) {
                TransferModel model = new TransferModel();
                model.setTransPath(cursor.getString(cursor.getColumnIndex("transPath")));
                model.setTransHashCode(cursor.getString(cursor.getColumnIndex("hashcode")));
                model.setTransProgress(Integer.valueOf(cursor.getString(cursor.getColumnIndex("progress"))));
                model.setCurrentSize(cursor.getString(cursor.getColumnIndex("currentSize")));
                model.setTransType(cursor.getString(cursor.getColumnIndex("type")));
                model.setIsTransfered(cursor.getString(cursor.getColumnIndex("isTransfer")));
                list.add(model);
            }
            if (cursor != null && !cursor.isClosed())
                cursor.close();
//
        } catch (Exception e) {
            Utils.Sout("qeex", e);
        }

        return list;


    }


    /**
     * 查询对应传输类型的数量【未传输过的】
     * type !=null 根据type查询
     * type ==null 查询全部类型
     */
    public int queryTransferSizeByType(String type) {
        int size = 0;
        db = mDBHlper.getReadableDatabase();
        try {
            String selection;
            String[] selectionArgs;
            if (type != null) {
                selection = "isTransfer=? and type=?";
                selectionArgs = new String[]{"0", type};
            } else {
                selection = "isTransfer=?";
                selectionArgs = new String[]{"0"};
            }
            Cursor cursor = db.query(TBL_NAME, null, selection, selectionArgs, null, null, null);
            size = cursor.getCount();
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            return size;
        } catch (Exception e) {
            Utils.Sout("qeex", e);
            return size;
        }
    }


    /**
     * 获取未传输相关的列表[动态 加载首页 Fragment ]
     */
    public List<String> querydExistNoTransAllGroupBy() {
        List<String> list = new ArrayList<>();
        db = mDBHlper.getReadableDatabase();

        try {

            String selection = "isTransfer=?";
            String[] selectionArgs = new String[]{"0"};
            Cursor cursor = db.query(TBL_NAME, new String[]{"type"}, selection, selectionArgs, "type", null, null);
            while (cursor.moveToNext()) {

                String type = cursor.getString(cursor.getColumnIndex("type"));
                list.add(type);
            }
            if (cursor != null && !cursor.isClosed())
                cursor.close();
//
        } catch (Exception e) {
            Utils.Sout("qeex", e);
        }

        return list;

    }

    /**
     * 从传输列表中移除
     */
    public int deletTransferModel(TransferModel transModel) {
        db = mDBHlper.getWritableDatabase();
        try {
            int deleteLine = db.delete(TBL_NAME, "type=? and hashcode=?", new String[]{transModel.getTransType(), transModel.getTransHashCode()});
            if (deleteLine > 0) {
                upateTransferTypeSizeByType(transModel.getTransType());
            }
            Utils.Sout("del", deleteLine);
            return deleteLine;
        } catch (SQLException e) {
            Utils.Sout("delex", e);
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * TODO TBL_TRANSFERPROGRESSNAME[传输进程] 表操作
     */

    /**
     * 添加到传输进程队列中
     * TODO
     */
    public void insertToTransferProgress(List<TransferTypeModel> transferTypeModels) {

        List<TransferModel> transferModels;
        for (int i = 0; i < transferTypeModels.size(); i++) {
            transferModels = queryTransferToProgressByType(transferTypeModels.get(i).getTypeName());
            if (transferModels != null) {
                long line = 0;
                ContentValues values = new ContentValues();
                for (int j = 0; j < transferModels.size(); j++) {
                    TransferModel transferModel = transferModels.get(j);
                    values.put("type", transferModel.getTransType());
                    values.put("hashcode", transferModel.getTransHashCode() + "");
                    values.put("progress", transferModel.getTransProgress());
                    values.put("currentSize", transferModel.getCurrentSize());
                    values.put("fileSize", transferModel.getTransLength());
                    values.put("isTransfer", "0");
                    values.put("transPath", transferModel.getTransPath());
                    line = insert(values, TBL_TRANSFERPROGRESSNAME);
                }

            }

        }


    }

    /**
     * 根据传输类型查询
     * 添加到传输进程队列中
     * TODO
     */
    public void insertToTransferProgressByType(String type) {

        List<TransferModel> transferModels;
        transferModels = queryTransferToProgressByType(type);
        if (transferModels != null) {
            long line = 0;
            ContentValues values = new ContentValues();
            for (int j = 0; j < transferModels.size(); j++) {
                TransferModel transferModel = transferModels.get(j);
                values.put("type", transferModel.getTransType());
                values.put("hashcode", transferModel.getTransHashCode() + "");
                values.put("progress", transferModel.getTransProgress());
                values.put("currentSize", transferModel.getCurrentSize());
                values.put("fileSize", transferModel.getTransLength());
                values.put("isTransfer", "0");
                values.put("transPath", transferModel.getTransPath());
                line = insert(values, TBL_TRANSFERPROGRESSNAME);
            }

        }


    }

    /**
     * 从传输进程中删除该传输完成的信息
     */
    public void deletTransProgressModel(TransferProgressModel model) {
        db = mDBHlper.getWritableDatabase();
        try {
            int deleteLine = db.delete(TBL_TRANSFERPROGRESSNAME, "hashcode=?", new String[]{model.getTransHashCode()});
            Utils.Sout("del", deleteLine);
        } catch (SQLException e) {
            Utils.Sout("delex", e);
            e.printStackTrace();
        }
    }


    /**
     * 查询传输进程中存在的所有待传输数据
     * TODO
     */
    public List<TransferProgressModel> queryAllTransferProgress() {

        List<TransferProgressModel> list = new ArrayList<>();
        db = mDBHlper.getReadableDatabase();
        try {
            Cursor cursor = db.query(TBL_TRANSFERPROGRESSNAME, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                TransferProgressModel model = new TransferProgressModel();
                model.setTransPath(cursor.getString(cursor.getColumnIndex("transPath")));
                model.setTransHashCode(cursor.getString(cursor.getColumnIndex("hashcode")));
                model.setTransProgress(Integer.valueOf(cursor.getString(cursor.getColumnIndex("progress"))));
                model.setCurrentSize(cursor.getString(cursor.getColumnIndex("currentSize")));
                model.setTransLength(cursor.getString(cursor.getColumnIndex("fileSize")));
                model.setTransType(cursor.getString(cursor.getColumnIndex("type")));
                model.setIsTransfered(cursor.getString(cursor.getColumnIndex("isTransfer")));
                list.add(model);
            }
            if (cursor != null && !cursor.isClosed())
                cursor.close();
//
        } catch (Exception e) {
            Utils.Sout("qeex", e);
        }


        return list;
    }


}
