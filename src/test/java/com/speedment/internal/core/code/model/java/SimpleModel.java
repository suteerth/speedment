/**
 *
 * Copyright (c) 2006-2016, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.speedment.internal.core.code.model.java;

import com.speedment.Speedment;
import com.speedment.config.db.Column;
import com.speedment.config.db.Dbms;
import com.speedment.config.db.PrimaryKeyColumn;
import com.speedment.config.db.Project;
import com.speedment.config.db.Schema;
import com.speedment.config.db.Table;
import com.speedment.config.db.mapper.TypeMapper;
import com.speedment.config.db.mapper.identity.StringIdentityMapper;
import com.speedment.config.db.trait.HasName;
import static com.speedment.internal.codegen.util.Formatting.indent;
import com.speedment.internal.core.config.dbms.StandardDbmsType;
import com.speedment.internal.core.runtime.DefaultSpeedmentApplicationLifecycle;
import com.speedment.internal.util.document.DocumentTranscoder;
import static java.util.stream.Collectors.joining;
import java.util.stream.Stream;
import org.junit.Before;

/**
 *
 * @author pemi
 */
public abstract class SimpleModel {

    protected static final String TABLE_NAME = "user";
    protected static final String TABLE_NAME2 = "S_P";
    protected static final String SCHEMA_NAME = "mySchema";
    protected static final String COLUMN_NAME = "first_name";
    protected static final String COLUMN_NAME2 = "item";

    protected Speedment speedment;
    protected Project project;
    protected Dbms dbms;
    protected Schema schema;
    protected Table table;
    protected Column column;
    protected PrimaryKeyColumn pkColumn;
    protected Table table2;
    protected Column column2;

    private String quote(String s) {
        return "\"" + s + "\"";
    }

    private String name(String s) {
        return quote(HasName.NAME) + " : " + quote(s);
    }

    private String typeMapper(Class<? extends TypeMapper<?, ?>> tmc) {
        return quote(Column.TYPE_MAPPER) + " : " + quote(tmc.getName());
    }

    private String dbTypeName(String dbmsTypeName) {
        return quote(Dbms.TYPE_NAME) + " : " + quote(dbmsTypeName);
    }

    private String columnDatabaseType(String typeName) {
        return quote(Column.DATABASE_TYPE) + " : " + quote(typeName);
    }

    private String array(String name, String... s) {
        return quote(name) + " : [\n" + indent(Stream.of(s).collect(joining(",\n"))) + "\n]";
    }

    private String objectWithKey(String name, String... s) {
        return quote(name) + " : " + object(s);
    }

    private String object(String... s) {
        return "{\n" + indent(Stream.of(s).collect(joining(",\n"))) + "\n}";
    }

    @Before
    public void simpleModelTestSetUp() {

        final String json = "{"
            + objectWithKey(DocumentTranscoder.ROOT,
                name("myProject"),
                array(Project.DBMSES,
                    object(
                        name("myDbms"),
                        dbTypeName(StandardDbmsType.defaultType().getName()),
                        array(Dbms.SCHEMAS,
                            object(
                                name(SCHEMA_NAME),
                                array(Schema.TABLES,
                                    object(
                                        name(TABLE_NAME),
                                        array(Table.COLUMNS,
                                            object(
                                                name(COLUMN_NAME),
                                                typeMapper(StringIdentityMapper.class),
                                                columnDatabaseType(String.class.getName())
                                            )
                                        ),
                                        array(Table.PRIMARY_KEY_COLUMNS,
                                            object(
                                                name(COLUMN_NAME)
                                            )
                                        )
                                    ),
                                    object(
                                        name(TABLE_NAME2),
                                        array(Table.COLUMNS,
                                            object(
                                                name(COLUMN_NAME2),
                                                typeMapper(StringIdentityMapper.class),
                                                columnDatabaseType(String.class.getName())
                                            )
                                        ),
                                        array(Table.PRIMARY_KEY_COLUMNS,
                                            object(
                                                name(COLUMN_NAME2)
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
            + "}";

        //System.out.println(json);
        speedment = new DefaultSpeedmentApplicationLifecycle(json)
            .withCheckDatabaseConnectivity(false)
            .withValidateRuntimeConfig(false)
            .build();

        project = speedment.getProjectComponent().getProject();
        dbms = project.dbmses().findAny().get();
        schema = dbms.schemas().findAny().get();
        table = schema.tables().filter(t -> TABLE_NAME.equals(t.getName())).findAny().get();
        column = table.columns().findAny().get();
        pkColumn = table.primaryKeyColumns().findAny().get();

        table2 = schema.tables().filter(t -> TABLE_NAME2.equals(t.getName())).findAny().get();
        column2 = table2.columns().findAny().get();

        //System.out.println(project);
//        project = new ProjectImpl(speedment);
//        dbms = project.addNewDbms();
//        schema = dbms.addNewSchema();
//        table = schema.addNewTable();
//        column = table.addNewColumn();
//        pkColumn = table.addNewPrimaryKeyColumn();
//
//        project.setName("myProject");
//        dbms.setName("myDbms");
//        schema.setName("myCoolApp");
//        table.setName(TABLE_NAME);
//        column.setName(COLUMN_NAME);
//        pkColumn.setName(COLUMN_NAME);
    }
}
