package com.jedikv;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Index;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class AppDaoGenerator {

    private static Entity currencyTable;
    private static Entity currencyPairTable;
    private static Entity conversionItemTable;

    public static void main (String [] args) throws Exception {

        Schema schema = new Schema(1, "converter_db");
        schema.enableKeepSectionsByDefault();

        createCurrencyTable(schema);
        createCurrencyPairTable(schema);
        createConversionItem(schema);

        new DaoGenerator().generateAll(schema, args[0]);
    }

    public static void createCurrencyTable(Schema schema) {

        currencyTable = schema.addEntity("CurrencyEntity");
        currencyTable.addStringProperty("symbol");
        currencyTable.addStringProperty("name");
        currencyTable.addStringProperty("countryName");
        currencyTable.addLongProperty("numericCode").primaryKey();
        currencyTable.addStringProperty("code").unique().notNull();

        //For the main list linking the currency with the conversion item
        /*
        Entity conversionEntity = schema.addEntity("ConversionEntity");
        conversionEntity.addIdProperty();
        Property fkProperty = conversionEntity.addLongProperty("currency_id").getProperty();
        conversionEntity.addIntProperty("position");
        conversionEntity.addToOne(currencyTable, fkProperty, "currency_code");
        */
    }

    public static void createCurrencyPairTable(Schema schema) {

        //CurrencyPairEntity
        currencyPairTable = schema.addEntity("CurrencyPairEntity");
        //currencyPairTable.addStringProperty("pair").unique().notNull();
        currencyPairTable.addIdProperty();
        currencyPairTable.addDateProperty("created_date");
        currencyPairTable.addDateProperty("last_updated");
        Property fkSourceId = currencyPairTable.addLongProperty("source_currency").notNull().getProperty();
        Property fkTargetId = currencyPairTable.addLongProperty("target_currency").notNull().getProperty();
        currencyPairTable.addIntProperty("rate");


        currencyPairTable.addToOne(currencyTable, fkSourceId, "source_id");
        currencyPairTable.addToOne(currencyTable, fkTargetId, "target_id");

        Index indexUnique = new Index();
        indexUnique.addProperty(fkSourceId);
        indexUnique.addProperty(fkTargetId);
        indexUnique.makeUnique();
        currencyPairTable.addIndex(indexUnique);

    }


    public static void createConversionItem(Schema schema) {

        conversionItemTable = schema.addEntity("ConversionItem");
        conversionItemTable.addIdProperty();
        Property fkCurrencyPair = conversionItemTable.addLongProperty("pair_id").notNull().getProperty();
        conversionItemTable.addIntProperty("position");
        conversionItemTable.addToOne(currencyPairTable, fkCurrencyPair);
    }

}
