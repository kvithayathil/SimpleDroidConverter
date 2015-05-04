package com.jedikv;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class AppDaoGenerator {

    public static void main (String [] args) throws Exception {

        Schema schema = new Schema(1, "converter_db");
        schema.enableKeepSectionsByDefault();

        createCurrencyTable(schema);
        createCurrencyPairTable(schema);

        new DaoGenerator().generateAll(schema, args[0]);
    }

    public static void createCurrencyTable(Schema schema) {

        Entity entity = schema.addEntity("CurrencyEntity");
        entity.addStringProperty("symbol");
        entity.addStringProperty("name");
        entity.addStringProperty("countryName");
        entity.addLongProperty("numericCode").primaryKey();
        entity.addStringProperty("code").unique().notNull();
    }

    public static void createCurrencyPairTable(Schema schema) {

        Entity entity = schema.addEntity("CurrencyPairEntity");
        entity.addStringProperty("pair").unique().notNull();
        entity.addIdProperty();
        entity.addDateProperty("date");
        entity.addIntProperty("rate");
    }
}
