
package com.messi.languagehelper.box;

import io.objectbox.BoxStore;
import io.objectbox.BoxStoreBuilder;
import io.objectbox.ModelBuilder;
import io.objectbox.ModelBuilder.EntityBuilder;
import io.objectbox.model.PropertyFlags;
import io.objectbox.model.PropertyType;

// THIS CODE IS GENERATED BY ObjectBox, DO NOT EDIT.
/**
 * Starting point for working with your ObjectBox. All boxes are set up for your objects here.
 * <p>
 * First steps (Android): get a builder using {@link #builder()}, call {@link BoxStoreBuilder#androidContext(Object)},
 * and {@link BoxStoreBuilder#build()} to get a {@link BoxStore} to work with.
 */
public class MyObjectBox {

    public static BoxStoreBuilder builder() {
        BoxStoreBuilder builder = new BoxStoreBuilder(getModel());
        builder.entity(EveryDaySentence_.__INSTANCE);
        builder.entity(ReadingSubject_.__INSTANCE);
        builder.entity(MomentLikes_.__INSTANCE);
        builder.entity(Reading_.__INSTANCE);
        builder.entity(WebFilter_.__INSTANCE);
        builder.entity(CNWBean_.__INSTANCE);
        return builder;
    }

    private static byte[] getModel() {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.lastEntityId(6, 2540749559985781639L);
        modelBuilder.lastIndexId(6, 8964642229373003809L);
        modelBuilder.lastRelationId(0, 0L);

        buildEntityEveryDaySentence(modelBuilder);
        buildEntityReadingSubject(modelBuilder);
        buildEntityMomentLikes(modelBuilder);
        buildEntityReading(modelBuilder);
        buildEntityWebFilter(modelBuilder);
        buildEntityCNWBean(modelBuilder);

        return modelBuilder.build();
    }

    private static void buildEntityEveryDaySentence(ModelBuilder modelBuilder) {
        EntityBuilder entityBuilder = modelBuilder.entity("EveryDaySentence");
        entityBuilder.id(6, 2540749559985781639L).lastPropertyId(20, 6356815833324233135L);
        entityBuilder.flags(io.objectbox.model.EntityFlags.USE_NO_ARG_CONSTRUCTOR);

        entityBuilder.property("id", PropertyType.Long).id(1, 4000703811679949338L)
                .flags(PropertyFlags.ID | PropertyFlags.NON_PRIMITIVE_TYPE);
        entityBuilder.property("cid", PropertyType.Long).id(2, 5212399036678429532L)
                .flags(PropertyFlags.NON_PRIMITIVE_TYPE | PropertyFlags.INDEXED).indexId(6, 8964642229373003809L);
        entityBuilder.property("sid", PropertyType.String).id(3, 4852473764460010393L);
        entityBuilder.property("tts", PropertyType.String).id(4, 6961644872488921109L);
        entityBuilder.property("tts_local_position", PropertyType.String).id(5, 4283556244200015701L);
        entityBuilder.property("content", PropertyType.String).id(6, 2168445809908692725L);
        entityBuilder.property("note", PropertyType.String).id(7, 1708670981297164327L);
        entityBuilder.property("love", PropertyType.String).id(8, 2382725943754514016L);
        entityBuilder.property("translation", PropertyType.String).id(9, 5865282151966605775L);
        entityBuilder.property("picture", PropertyType.String).id(10, 8679941119499242410L);
        entityBuilder.property("picture2", PropertyType.String).id(11, 9136388835342068318L);
        entityBuilder.property("caption", PropertyType.String).id(12, 7002535316068476952L);
        entityBuilder.property("dateline", PropertyType.String).id(13, 8560333187955941615L);
        entityBuilder.property("s_pv", PropertyType.String).id(14, 6526489872934797008L);
        entityBuilder.property("sp_pv", PropertyType.String).id(15, 4324827295705783301L);
        entityBuilder.property("fenxiang_img", PropertyType.String).id(16, 8959820756908705127L);
        entityBuilder.property("fenxiang_img_local_position", PropertyType.String).id(17, 8518750335366692397L);
        entityBuilder.property("backup1", PropertyType.String).id(18, 4535431592604435183L);
        entityBuilder.property("backup2", PropertyType.String).id(19, 2171478550620344325L);
        entityBuilder.property("backup3", PropertyType.String).id(20, 6356815833324233135L);


        entityBuilder.entityDone();
    }

    private static void buildEntityReadingSubject(ModelBuilder modelBuilder) {
        EntityBuilder entityBuilder = modelBuilder.entity("ReadingSubject");
        entityBuilder.id(3, 3110430987322842181L).lastPropertyId(8, 5255918259572070149L);
        entityBuilder.flags(io.objectbox.model.EntityFlags.USE_NO_ARG_CONSTRUCTOR);

        entityBuilder.property("id", PropertyType.Long).id(1, 2227665014556694129L)
                .flags(PropertyFlags.ID);
        entityBuilder.property("objectId", PropertyType.String).id(2, 2967640124154169069L)
                .flags(PropertyFlags.INDEX_HASH).indexId(3, 6815057945743640498L);
        entityBuilder.property("name", PropertyType.String).id(3, 5821272637059367913L);
        entityBuilder.property("category", PropertyType.String).id(4, 7575922884050837893L);
        entityBuilder.property("level", PropertyType.String).id(5, 616399869417557875L);
        entityBuilder.property("code", PropertyType.String).id(6, 8557705001234428064L);
        entityBuilder.property("order", PropertyType.String).id(7, 7453984672991539501L);
        entityBuilder.property("creat_time", PropertyType.Long).id(8, 5255918259572070149L)
                .flags(PropertyFlags.NOT_NULL);


        entityBuilder.entityDone();
    }

    private static void buildEntityMomentLikes(ModelBuilder modelBuilder) {
        EntityBuilder entityBuilder = modelBuilder.entity("MomentLikes");
        entityBuilder.id(4, 8533631915347739076L).lastPropertyId(2, 4608160211459173027L);
        entityBuilder.flags(io.objectbox.model.EntityFlags.USE_NO_ARG_CONSTRUCTOR);

        entityBuilder.property("id", PropertyType.Long).id(1, 8256135753634057432L)
                .flags(PropertyFlags.ID);
        entityBuilder.property("moments_id", PropertyType.String).id(2, 4608160211459173027L)
                .flags(PropertyFlags.INDEX_HASH).indexId(4, 376579638095034348L);


        entityBuilder.entityDone();
    }

    private static void buildEntityReading(ModelBuilder modelBuilder) {
        EntityBuilder entityBuilder = modelBuilder.entity("Reading");
        entityBuilder.id(5, 2508500946353572578L).lastPropertyId(34, 1473431717324137965L);
        entityBuilder.flags(io.objectbox.model.EntityFlags.USE_NO_ARG_CONSTRUCTOR);

        entityBuilder.property("id", PropertyType.Long).id(1, 285723131730538682L)
                .flags(PropertyFlags.ID | PropertyFlags.NON_PRIMITIVE_TYPE);
        entityBuilder.property("object_id", PropertyType.String).id(2, 551212942247870413L)
                .flags(PropertyFlags.INDEX_HASH).indexId(5, 773016095715578220L);
        entityBuilder.property("title", PropertyType.String).id(3, 8823048486319672952L);
        entityBuilder.property("source_url", PropertyType.String).id(4, 4898235724899489399L);
        entityBuilder.property("content", PropertyType.String).id(5, 4869325399822336325L);
        entityBuilder.property("item_id", PropertyType.String).id(6, 1273802165544673630L);
        entityBuilder.property("media_url", PropertyType.String).id(7, 3605995181612598296L);
        entityBuilder.property("source_name", PropertyType.String).id(8, 3217325254015283399L);
        entityBuilder.property("publish_time", PropertyType.String).id(9, 7325734188521795264L);
        entityBuilder.property("type_name", PropertyType.String).id(10, 3816717955391878687L);
        entityBuilder.property("img_type", PropertyType.String).id(11, 1377958957730342290L);
        entityBuilder.property("img_url", PropertyType.String).id(12, 9166350223421488819L);
        entityBuilder.property("type", PropertyType.String).id(13, 4222408537608499101L);
        entityBuilder.property("category", PropertyType.String).id(14, 980981502405146847L);
        entityBuilder.property("type_id", PropertyType.String).id(15, 5482689641674708407L);
        entityBuilder.property("level", PropertyType.String).id(16, 4971060756504629188L);
        entityBuilder.property("content_type", PropertyType.String).id(17, 1266954597924058740L);
        entityBuilder.property("img_urls", PropertyType.String).id(18, 5818153007733697824L);
        entityBuilder.property("status", PropertyType.String).id(19, 6561638219100361649L);
        entityBuilder.property("isCollected", PropertyType.String).id(20, 1891068367028050793L);
        entityBuilder.property("collected_time", PropertyType.Long).id(21, 7701890528733873333L)
                .flags(PropertyFlags.NOT_NULL);
        entityBuilder.property("isReadLater", PropertyType.String).id(22, 4326131630010840730L);
        entityBuilder.property("read_later_time", PropertyType.Long).id(23, 1154199971169977353L)
                .flags(PropertyFlags.NOT_NULL);
        entityBuilder.property("like", PropertyType.Int).id(24, 5479537078395807009L)
                .flags(PropertyFlags.NOT_NULL);
        entityBuilder.property("unlike", PropertyType.Int).id(25, 8588253206143124736L)
                .flags(PropertyFlags.NOT_NULL);
        entityBuilder.property("comments", PropertyType.Int).id(26, 2107785779940496315L)
                .flags(PropertyFlags.NOT_NULL);
        entityBuilder.property("readed", PropertyType.Int).id(27, 5002844217762590927L)
                .flags(PropertyFlags.NOT_NULL);
        entityBuilder.property("lrc_url", PropertyType.String).id(28, 2277651920543987450L);
        entityBuilder.property("backup1", PropertyType.String).id(29, 5569240504885109575L);
        entityBuilder.property("backup2", PropertyType.String).id(30, 4694043225588618839L);
        entityBuilder.property("backup3", PropertyType.String).id(31, 6193053853166472800L);
        entityBuilder.property("backup4", PropertyType.String).id(32, 5522812555591854668L);
        entityBuilder.property("backup5", PropertyType.String).id(33, 4132873096433260115L);
        entityBuilder.property("img_color", PropertyType.Int).id(34, 1473431717324137965L)
                .flags(PropertyFlags.NOT_NULL);


        entityBuilder.entityDone();
    }

    private static void buildEntityWebFilter(ModelBuilder modelBuilder) {
        EntityBuilder entityBuilder = modelBuilder.entity("WebFilter");
        entityBuilder.id(1, 3496996332654680549L).lastPropertyId(5, 4087304507087629831L);
        entityBuilder.flags(io.objectbox.model.EntityFlags.USE_NO_ARG_CONSTRUCTOR);

        entityBuilder.property("id", PropertyType.Long).id(1, 2166657811223778543L)
                .flags(PropertyFlags.ID);
        entityBuilder.property("name", PropertyType.String).id(2, 4380733867604384059L)
                .flags(PropertyFlags.INDEX_HASH).indexId(1, 5945815078337994721L);
        entityBuilder.property("ad_filter", PropertyType.String).id(3, 5196970846364399690L);
        entityBuilder.property("ad_url", PropertyType.String).id(4, 3329399293496612082L);
        entityBuilder.property("is_show_ad", PropertyType.String).id(5, 4087304507087629831L);


        entityBuilder.entityDone();
    }

    private static void buildEntityCNWBean(ModelBuilder modelBuilder) {
        EntityBuilder entityBuilder = modelBuilder.entity("CNWBean");
        entityBuilder.id(2, 1588007591114287449L).lastPropertyId(28, 4958165198420979623L);
        entityBuilder.flags(io.objectbox.model.EntityFlags.USE_NO_ARG_CONSTRUCTOR);

        entityBuilder.property("id", PropertyType.Long).id(1, 7038038043524837647L)
                .flags(PropertyFlags.ID);
        entityBuilder.property("itemId", PropertyType.String).id(2, 2524512898384231795L)
                .flags(PropertyFlags.INDEX_HASH).indexId(2, 7586579419349449235L);
        entityBuilder.property("table", PropertyType.String).id(3, 2768999903678305748L);
        entityBuilder.property("category", PropertyType.String).id(4, 8214678972461591553L);
        entityBuilder.property("type", PropertyType.String).id(5, 2075167676901450736L);
        entityBuilder.property("title", PropertyType.String).id(6, 4526464211455017248L);
        entityBuilder.property("author", PropertyType.String).id(7, 2434538024279558200L);
        entityBuilder.property("tag", PropertyType.String).id(8, 2209866864994209187L);
        entityBuilder.property("des", PropertyType.String).id(9, 924947428896527519L);
        entityBuilder.property("is_free", PropertyType.String).id(10, 8484523933627656383L);
        entityBuilder.property("sub_title", PropertyType.String).id(11, 3518218467800882285L);
        entityBuilder.property("update_des", PropertyType.String).id(12, 3366280876305008570L);
        entityBuilder.property("img_url", PropertyType.String).id(13, 273740992621864706L);
        entityBuilder.property("imgs_url", PropertyType.String).id(14, 9093910642256310693L);
        entityBuilder.property("read_url", PropertyType.String).id(15, 1147015702527005590L);
        entityBuilder.property("last_read_url", PropertyType.String).id(16, 7881756030252660093L);
        entityBuilder.property("source_url", PropertyType.String).id(17, 576687549702454248L);
        entityBuilder.property("source_name", PropertyType.String).id(18, 8963374591126106550L);
        entityBuilder.property("json_str", PropertyType.String).id(19, 3515173974528838616L);
        entityBuilder.property("key", PropertyType.String).id(20, 8928489627415165609L);
        entityBuilder.property("collected", PropertyType.Long).id(21, 7240929029495409042L)
                .flags(PropertyFlags.NOT_NULL);
        entityBuilder.property("history", PropertyType.Long).id(22, 8869005392623960205L)
                .flags(PropertyFlags.NOT_NULL);
        entityBuilder.property("view", PropertyType.Long).id(23, 2077428430813608190L)
                .flags(PropertyFlags.NOT_NULL);
        entityBuilder.property("ceateTime", PropertyType.Long).id(24, 8335085192427932866L)
                .flags(PropertyFlags.NOT_NULL);
        entityBuilder.property("updateTime", PropertyType.Long).id(25, 7946318109457714163L)
                .flags(PropertyFlags.NOT_NULL);
        entityBuilder.property("backup1", PropertyType.String).id(26, 8601297499929040182L);
        entityBuilder.property("backup2", PropertyType.String).id(27, 7112001946371936872L);
        entityBuilder.property("backup3", PropertyType.String).id(28, 4958165198420979623L);


        entityBuilder.entityDone();
    }


}
