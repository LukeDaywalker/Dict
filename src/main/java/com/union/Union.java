package com.union;

import org.sqlite.SQLiteException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LukeSkywalker on 2016/12/22.
 */
public class Union {
    public static void main(String[] args) {
        saveUnionDB();
    }

    private static void saveUnionDB() {
        try {
            //连接SQLite的JDBC

            List<UnionWord> unionWordList = new ArrayList<UnionWord>();
            Class.forName("org.sqlite.JDBC");

            //建立一个数据库名union.db的连接，如果不存在就在当前目录下创建之

            Connection conn = DriverManager.getConnection("jdbc:sqlite:union.db");

            Statement stat = conn.createStatement();

            ResultSet rs = stat.executeQuery("SELECT * FROM five_elements,kx_word WHERE five_elements.word=kx_word.word;");
            while (rs.next()) {
                String word = rs.getString("word");
                String jt = rs.getString("jt");
                String ft = rs.getString("ft");
                int cc1 = rs.getInt("cc1");
                int cc2 = rs.getInt("cc2");
                int tc = rs.getInt("tc");
                int ty = rs.getInt("ty");
                String py = rs.getString("py");
                String tone = rs.getString("tone");
                String pyt = rs.getString("pyt");
                int duoYin = rs.getInt("duoYin");

                String shengMu = rs.getString("shengMu");
                String yunTou = rs.getString("yunTou");
                String yunFu = rs.getString("yunFu");
                String yunWei = rs.getString("yunWei");


                String kxWord = rs.getString("kxWord");
                String wordSet = rs.getString("wordSet");
                String radical = rs.getString("radical");
                int kxAllStork = rs.getInt("kxAllStork");
                int kxOtherStork = rs.getInt("kxOtherStork");
                int isSurname = rs.getInt("isSurname");
                String fiveElements = rs.getString("fiveElements");
                String goodOrIll = rs.getString("goodOrIll");
                UnionWord unionWord = new UnionWord(word, jt, ft, cc1, cc2, tc, ty,
                        py, tone, pyt, duoYin,
                        shengMu, yunTou, yunFu, yunWei,
                        kxWord, wordSet, radical, kxAllStork, kxOtherStork, isSurname, fiveElements, goodOrIll);
                unionWordList.add(unionWord);
            }
            rs.close();

            stat.executeUpdate("create table IF NOT EXISTS  union_word (word  VARCHAR UNIQUE, jt  VARCHAR, ft  VARCHAR, cc1 INTEGER, cc2 INTEGER, tc INTEGER, ty INTEGER," +
                    " py  VARCHAR, tone  VARCHAR, pyt  VARCHAR, duoYin INTEGER," +
                    " shengMu  VARCHAR,yunTou  VARCHAR,yunFu  VARCHAR,yunWei  VARCHAR," +
                    " kxWord  VARCHAR, wordSet  VARCHAR, radical  VARCHAR, kxAllStork INTEGER, kxOtherStork INTEGER,  isSurname INTEGER,  fiveElements  VARCHAR, goodOrIll  VARCHAR);");
            PreparedStatement prep = conn.prepareStatement(
                    "replace into union_word values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            for (UnionWord word : unionWordList) {
                prep.setString(1, word.getWord());
                prep.setString(2, word.getJt());
                prep.setString(3, word.getFt());
                prep.setInt(4, word.getCc1());
                prep.setInt(5, word.getCc2());
                prep.setInt(6, word.getTc());
                prep.setInt(7, word.getTy());
                prep.setString(8, word.getPy());
                prep.setString(9, word.getTone());
                prep.setString(10, word.getPyt());
                prep.setInt(11, word.getDuoYin());

                prep.setString(12, word.getShengMu());
                prep.setString(13, word.getYunTou());
                prep.setString(14, word.getYunFu());
                prep.setString(15, word.getYunWei());

                prep.setString(16, word.getKxWord());
                prep.setString(17, word.getWordSet());
                prep.setString(18, word.getRadical());
                prep.setInt(19, word.getKxAllStork());
                prep.setInt(20, word.getKxOtherStork());

                prep.setInt(21, word.getIsSurname());
                prep.setString(22, word.getFiveElements());
                prep.setString(23, word.getGoodOrIll());
                prep.addBatch();
            }

            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);

            conn.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
