package org.jacoco.report.common;
/*******************************************************************************
 * Copyright (c) 2009, 2019 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    John Keeping - initial implementation
 *    Marc R. Hoffmann - rework
 *
 *******************************************************************************/

import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;
import java.sql.DriverManager;


public class DBTools {

    private static Connection conn = null;

    static {
        System.out.println("DBTools static执行了。。。。。。。。。。。。。。。。");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //conn = DriverManager.getConnection("jdbc:mysql://10.92.3.44:3306/wfc_qa_test?useUnicode=true&amp;characterEncoding=UTF-8", "wfc_qa_test", "#9aVAzdJ");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wfc_qa_test?useUnicode=true&characterEncoding=utf8", "root", "123456");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public DBTools(){
        System.out.println("DBTools 构造方法执行了。。。。。。。。。。。。。。。。");

    }


    public static void initCon(){
        System.out.println("要执行初始化过程了");

        try {
            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection("jdbc:mysql://10.92.3.44:3306/wfc_qa_test?useUnicode=true&amp;characterEncoding=UTF-8", "wfc_qa_test", "#9aVAzdJ");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wfc_qa_test?useUnicode=true&characterEncoding=utf8", "root", "123456");
            if(conn !=null){
                System.out.println("初始化成功");
            }
        }catch (Exception e){
            System.out.println("执行初始化失败");
            System.out.println(e.getMessage());
            System.out.println(e.getCause().toString());
            e.printStackTrace();
        }
    }

    public static void insertDB(String sql,Object[] params){
        try {
            QueryRunner runner = new QueryRunner();
            int i = runner.update(conn, sql,params);
            if (i !=1){
                new Exception("sql插入失败");
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        finally {
            new Exception("sql执行错误");

        }
    }
}
