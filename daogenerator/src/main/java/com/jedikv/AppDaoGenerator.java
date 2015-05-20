package com.jedikv;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
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

        Entity currencyEntity = schema.addEntity("CurrencyEntity");
        currencyEntity.addStringProperty("symbol");
        currencyEntity.addStringProperty("name");
        currencyEntity.addStringProperty("countryName");
        currencyEntity.addLongProperty("numericCode").primaryKey();
        currencyEntity.addStringProperty("code").unique().notNull();

        //For the main list linking the currency with the conversion item
        Entity conversionEntity = schema.addEntity("ConversionEntity");
        conversionEntity.addIdProperty();
        Property fkProperty = conversionEntity.addLongProperty("currency_id").getProperty();
        conversionEntity.addIntProperty("position");
        conversionEntity.addToOne(currencyEntity, fkProperty, "currency_code");
    }

    public static void createCurrencyPairTable(Schema schema) {

        //CurrencyPairEntity
        Entity currencyPair = schema.addEntity("CurrencyPairEntity");
        currencyPair.addStringProperty("pair").unique().notNull();
        currencyPair.addIdProperty();
        currencyPair.addDateProperty("created_date");
        currencyPair.addDateProperty("date");
        currencyPair.addIntProperty("rate");


    }

}
