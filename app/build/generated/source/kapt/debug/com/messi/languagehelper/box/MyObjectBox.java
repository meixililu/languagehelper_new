
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
        builder.entity(TranResultZhYue_.__INSTANCE);
        builder.entity(WordDetailListItem_.__INSTANCE);
        builder.entity(Dictionary_.__INSTANCE);
        builder.entity(Reading_.__INSTANCE);
        builder.entity(AiEntity_.__INSTANCE);
        builder.entity(Record_.__INSTANCE);
        builder.entity(WebFilter_.__INSTANCE);
        builder.entity(CNWBean_.__INSTANCE);
        builder.entity(CollectedData_.__INSTANCE);
        builder.entity(CourseList_.__INSTANCE);
        return builder;
    }

    private static byte[] getModel() {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.lastEntityId(15, 1683819082702328425L);
        modelBuilder.lastIndexId(15, 1910611769934093760L);
        modelBuilder.lastRelationId(0, 0L);

        buildEntityEveryDaySentence(modelBuilder);
        buildEntityReadingSubject(modelBuilder);
        buildEntityMomentLikes(modelBuilder);
        buildEntityTranResultZhYue(modelBuilder);
        buildEntityWordDetailListItem(modelBuilder);
        buildEntityDictionary(modelBuilder);
        buildEntityReading(modelBuilder);
        buildEntityAiEntity(modelBuilder);
        buildEntityRecord(modelBuilder);
        buildEntityWebFilter(modelBuilder);
        buildEntityCNWBean(modelBuilder);
        buildEntityCollectedData(modelBuilder);
        buildEntityCourseList(modelBuilder);

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
        entityBuilder.id(3, 3110430987322842181L).lastPropertyId(12, 7660327528341797608L);
        entityBuilder.flags(io.objectbox.model.EntityFlags.USE_NO_ARG_CONSTRUCTOR);

        entityBuilder.property("id", PropertyType.Long).id(1, 2227665014556694129L)
                .flags(PropertyFlags.ID);
        entityBuilder.property("objectId", PropertyType.String).id(2, 2967640124154169069L)
                .flags(PropertyFlags.INDEX_HASH).indexId(3, 6815057945743640498L);
        entityBuilder.property("name", PropertyType.String).id(3, 5821272637059367913L);
        entityBuilder.property("category", PropertyType.String).id(4, 7575922884050837893L);
        entityBuilder.property("source_name", PropertyType.String).id(9, 4210885046754564135L);
        entityBuilder.property("source_url", PropertyType.String).id(10, 7768858810324117761L);
        entityBuilder.property("imgId", PropertyType.Int).id(12, 7660327528341797608L)
                .flags(PropertyFlags.NOT_NULL);
        entityBuilder.property("level", PropertyType.String).id(5, 616399869417557875L);
        entityBuilder.property("code", PropertyType.String).id(6, 8557705001234428064L);
        entityBuilder.property("order", PropertyType.String).id(7, 7453984672991539501L);
        entityBuilder.property("views", PropertyType.Int).id(11, 9127389154030167286L)
                .flags(PropertyFlags.NOT_NULL);
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

    private static void buildEntityTranResultZhYue(ModelBuilder modelBuilder) {
        EntityBuilder entityBuilder = modelBuilder.entity("TranResultZhYue");
        entityBuilder.id(8, 4682402178279665778L).lastPropertyId(13, 8017023710255767855L);
        entityBuilder.flags(io.objectbox.model.EntityFlags.USE_NO_ARG_CONSTRUCTOR);

        entityBuilder.property("id", PropertyType.Long).id(1, 966485390786207412L)
                .flags(PropertyFlags.ID | PropertyFlags.NON_PRIMITIVE_TYPE);
        entityBuilder.property("english", PropertyType.String).id(2, 3527192576848670054L);
        entityBuilder.property("chinese", PropertyType.String).id(3, 5833774742858936856L);
        entityBuilder.property("resultAudioPath", PropertyType.String).id(4, 265904221198818351L);
        entityBuilder.property("questionAudioPath", PropertyType.String).id(5, 7306545938294440408L);
        entityBuilder.property("questionVoiceId", PropertyType.String).id(6, 8464473958152678L);
        entityBuilder.property("resultVoiceId", PropertyType.String).id(7, 525050025254040979L);
        entityBuilder.property("iscollected", PropertyType.String).id(8, 8551248983494533992L);
        entityBuilder.property("visit_times", PropertyType.Int).id(9, 2032801358635142484L)
                .flags(PropertyFlags.NON_PRIMITIVE_TYPE);
        entityBuilder.property("speak_speed", PropertyType.Int).id(10, 157996006288648334L)
                .flags(PropertyFlags.NON_PRIMITIVE_TYPE);
        entityBuilder.property("backup1", PropertyType.String).id(11, 1708073692157171760L);
        entityBuilder.property("backup2", PropertyType.String).id(12, 7956457746621751846L);
        entityBuilder.property("backup3", PropertyType.String).id(13, 8017023710255767855L);


        entityBuilder.entityDone();
    }

    private static void buildEntityWordDetailListItem(ModelBuilder modelBuilder) {
        EntityBuilder entityBuilder = modelBuilder.entity("WordDetailListItem");
        entityBuilder.id(10, 5520583404938970285L).lastPropertyId(31, 7488594298769615867L);
        entityBuilder.flags(io.objectbox.model.EntityFlags.USE_NO_ARG_CONSTRUCTOR);

        entityBuilder.property("id", PropertyType.Long).id(1, 5536262299464011122L)
                .flags(PropertyFlags.ID | PropertyFlags.NON_PRIMITIVE_TYPE);
        entityBuilder.property("item_id", PropertyType.String).id(2, 6067960713186243414L)
                .flags(PropertyFlags.INDEX_HASH).indexId(11, 8390970880567877118L);
        entityBuilder.property("class_id", PropertyType.String).id(3, 4202399373662515236L)
                .flags(PropertyFlags.INDEX_HASH).indexId(7, 4982277349408116855L);
        entityBuilder.property("course", PropertyType.Int).id(4, 9143143455136014634L)
                .flags(PropertyFlags.NON_PRIMITIVE_TYPE | PropertyFlags.INDEXED).indexId(10, 1046166335712526561L);
        entityBuilder.property("class_title", PropertyType.String).id(5, 4255567185116938670L);
        entityBuilder.property("desc", PropertyType.String).id(6, 3124864942665235801L);
        entityBuilder.property("name", PropertyType.String).id(7, 3539775361740300911L)
                .flags(PropertyFlags.INDEX_HASH).indexId(8, 2954847185003916394L);
        entityBuilder.property("sound", PropertyType.String).id(8, 2119515828894997809L);
        entityBuilder.property("symbol", PropertyType.String).id(9, 3988950402356991207L);
        entityBuilder.property("examples", PropertyType.String).id(10, 6640013367239851092L);
        entityBuilder.property("mp3_sdpath", PropertyType.String).id(11, 2090766195212357400L);
        entityBuilder.property("img_url", PropertyType.String).id(12, 6345076305510957798L);
        entityBuilder.property("new_words", PropertyType.String).id(13, 909346753189105032L);
        entityBuilder.property("is_study", PropertyType.String).id(14, 7530004958251196410L);
        entityBuilder.property("level", PropertyType.String).id(25, 8940678696548379293L);
        entityBuilder.property("paraphrase", PropertyType.String).id(18, 2953624702961883263L);
        entityBuilder.property("en_paraphrase", PropertyType.String).id(19, 768308749514356744L);
        entityBuilder.property("au_paraphrase", PropertyType.String).id(20, 5985599134920520413L);
        entityBuilder.property("dicts", PropertyType.String).id(21, 3509462992078116178L);
        entityBuilder.property("examinations", PropertyType.String).id(22, 2138412019413807586L);
        entityBuilder.property("root", PropertyType.String).id(23, 2548303408524480478L);
        entityBuilder.property("tense", PropertyType.String).id(24, 430683084615524676L);
        entityBuilder.property("type", PropertyType.String).id(31, 7488594298769615867L);
        entityBuilder.property("is_know", PropertyType.Bool).id(26, 3941164611644040828L)
                .flags(PropertyFlags.NOT_NULL);
        entityBuilder.property("backup1", PropertyType.String).id(15, 4697637849037597874L);
        entityBuilder.property("backup2", PropertyType.String).id(16, 485770395444591549L);
        entityBuilder.property("backup3", PropertyType.String).id(17, 8406401615325982418L);
        entityBuilder.property("backup4", PropertyType.String).id(27, 7159838489314322770L);
        entityBuilder.property("backup5", PropertyType.String).id(28, 335172138582113055L);
        entityBuilder.property("backup6", PropertyType.String).id(29, 4784337309700006298L);
        entityBuilder.property("backup7", PropertyType.String).id(30, 1009891971319804626L);


        entityBuilder.entityDone();
    }

    private static void buildEntityDictionary(ModelBuilder modelBuilder) {
        EntityBuilder entityBuilder = modelBuilder.entity("Dictionary");
        entityBuilder.id(9, 6838911499641689906L).lastPropertyId(21, 8449115134472958357L);
        entityBuilder.flags(io.objectbox.model.EntityFlags.USE_NO_ARG_CONSTRUCTOR);

        entityBuilder.property("id", PropertyType.Long).id(1, 2367457257654218260L)
                .flags(PropertyFlags.ID | PropertyFlags.NON_PRIMITIVE_TYPE);
        entityBuilder.property("word_name", PropertyType.String).id(2, 2787310737100162923L);
        entityBuilder.property("result", PropertyType.String).id(3, 6511875222049408128L);
        entityBuilder.property("to_lan", PropertyType.String).id(4, 40406947459177950L);
        entityBuilder.property("from_lan", PropertyType.String).id(5, 4564165103155642366L);
        entityBuilder.property("ph_am", PropertyType.String).id(6, 2375629368821158773L);
        entityBuilder.property("ph_en", PropertyType.String).id(7, 7299903868425284175L);
        entityBuilder.property("ph_zh", PropertyType.String).id(8, 7656458362687864090L);
        entityBuilder.property("type", PropertyType.String).id(9, 2681638153163358075L);
        entityBuilder.property("questionVoiceId", PropertyType.String).id(10, 1115359391819702492L);
        entityBuilder.property("questionAudioPath", PropertyType.String).id(11, 8869982685041075486L);
        entityBuilder.property("resultVoiceId", PropertyType.String).id(12, 6797783759278940885L);
        entityBuilder.property("resultAudioPath", PropertyType.String).id(13, 4604134020326478345L);
        entityBuilder.property("iscollected", PropertyType.String).id(14, 7506455842305574926L);
        entityBuilder.property("visit_times", PropertyType.Int).id(15, 9001849647335953102L)
                .flags(PropertyFlags.NON_PRIMITIVE_TYPE);
        entityBuilder.property("speak_speed", PropertyType.Int).id(16, 1152237384049697104L)
                .flags(PropertyFlags.NON_PRIMITIVE_TYPE);
        entityBuilder.property("backup1", PropertyType.String).id(17, 8886892295037433528L);
        entityBuilder.property("backup2", PropertyType.String).id(18, 3593715694352620464L);
        entityBuilder.property("backup3", PropertyType.String).id(19, 3906121923668999608L);
        entityBuilder.property("backup4", PropertyType.String).id(20, 8920232999331611738L);
        entityBuilder.property("backup5", PropertyType.String).id(21, 8449115134472958357L);


        entityBuilder.entityDone();
    }

    private static void buildEntityReading(ModelBuilder modelBuilder) {
        EntityBuilder entityBuilder = modelBuilder.entity("Reading");
        entityBuilder.id(5, 2508500946353572578L).lastPropertyId(37, 2770654339950208330L);
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
        entityBuilder.property("category_2", PropertyType.String).id(37, 2770654339950208330L);
        entityBuilder.property("type_id", PropertyType.String).id(15, 5482689641674708407L);
        entityBuilder.property("level", PropertyType.String).id(16, 4971060756504629188L);
        entityBuilder.property("content_type", PropertyType.String).id(17, 1266954597924058740L);
        entityBuilder.property("img_urls", PropertyType.String).id(18, 5818153007733697824L);
        entityBuilder.property("status", PropertyType.String).id(19, 6561638219100361649L);
        entityBuilder.property("vid", PropertyType.String).id(35, 8429725476707112570L);
        entityBuilder.property("isCollected", PropertyType.String).id(20, 1891068367028050793L);
        entityBuilder.property("boutique_code", PropertyType.String).id(36, 5777419081988935434L);
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

    private static void buildEntityAiEntity(ModelBuilder modelBuilder) {
        EntityBuilder entityBuilder = modelBuilder.entity("AiEntity");
        entityBuilder.id(11, 8894725922907501287L).lastPropertyId(19, 1817635438217159979L);
        entityBuilder.flags(io.objectbox.model.EntityFlags.USE_NO_ARG_CONSTRUCTOR);

        entityBuilder.property("id", PropertyType.Long).id(1, 7970327623858342933L)
                .flags(PropertyFlags.ID | PropertyFlags.NON_PRIMITIVE_TYPE);
        entityBuilder.property("entity_type", PropertyType.String).id(2, 2367444971679165749L);
        entityBuilder.property("content_type", PropertyType.String).id(3, 7932131476880775464L);
        entityBuilder.property("role", PropertyType.String).id(4, 3347507820015877506L);
        entityBuilder.property("content", PropertyType.String).id(5, 5707395739008308989L);
        entityBuilder.property("content_video_id", PropertyType.String).id(6, 1541206873855323672L);
        entityBuilder.property("content_video_path", PropertyType.String).id(7, 2489986953695638185L);
        entityBuilder.property("img_url", PropertyType.String).id(8, 7777465472686390674L);
        entityBuilder.property("link", PropertyType.String).id(9, 811145638257408868L);
        entityBuilder.property("translate", PropertyType.String).id(10, 2211451054273878789L);
        entityBuilder.property("ai_type", PropertyType.String).id(11, 4539946209856486146L);
        entityBuilder.property("created", PropertyType.Long).id(12, 1004334341705689837L)
                .flags(PropertyFlags.NON_PRIMITIVE_TYPE);
        entityBuilder.property("backup1", PropertyType.String).id(13, 9142670773887643537L);
        entityBuilder.property("backup2", PropertyType.String).id(14, 2021903329141346371L);
        entityBuilder.property("backup3", PropertyType.String).id(15, 6133337627963246274L);
        entityBuilder.property("backup4", PropertyType.String).id(16, 429618497646478542L);
        entityBuilder.property("backup5", PropertyType.String).id(17, 8462812336103318081L);
        entityBuilder.property("backup6", PropertyType.String).id(18, 5935471564974580967L);
        entityBuilder.property("backup7", PropertyType.String).id(19, 1817635438217159979L);


        entityBuilder.entityDone();
    }

    private static void buildEntityRecord(ModelBuilder modelBuilder) {
        EntityBuilder entityBuilder = modelBuilder.entity("Record");
        entityBuilder.id(7, 2299188868989635804L).lastPropertyId(26, 6622587531338528559L);
        entityBuilder.flags(io.objectbox.model.EntityFlags.USE_NO_ARG_CONSTRUCTOR);

        entityBuilder.property("id", PropertyType.Long).id(1, 3622779869807933367L)
                .flags(PropertyFlags.ID | PropertyFlags.NON_PRIMITIVE_TYPE);
        entityBuilder.property("english", PropertyType.String).id(2, 2680978039210277411L);
        entityBuilder.property("chinese", PropertyType.String).id(3, 884082435267075603L);
        entityBuilder.property("resultAudioPath", PropertyType.String).id(4, 5000480740313718976L);
        entityBuilder.property("questionAudioPath", PropertyType.String).id(5, 5913073034574712234L);
        entityBuilder.property("questionVoiceId", PropertyType.String).id(6, 6688460902990046095L);
        entityBuilder.property("resultVoiceId", PropertyType.String).id(7, 575908343572342797L);
        entityBuilder.property("iscollected", PropertyType.String).id(8, 710434385512588605L);
        entityBuilder.property("ph_am_mp3", PropertyType.String).id(14, 8507070872464152587L);
        entityBuilder.property("ph_en_mp3", PropertyType.String).id(15, 3218347760857387349L);
        entityBuilder.property("ph_tts_mp3", PropertyType.String).id(16, 2563653877057901864L);
        entityBuilder.property("des", PropertyType.String).id(17, 8476786870835622317L);
        entityBuilder.property("examples", PropertyType.String).id(18, 349427129572319826L);
        entityBuilder.property("paraphrase", PropertyType.String).id(19, 3411933568354466244L);
        entityBuilder.property("en_paraphrase", PropertyType.String).id(20, 192852137869272928L);
        entityBuilder.property("au_paraphrase", PropertyType.String).id(21, 2587438913214855098L);
        entityBuilder.property("dicts", PropertyType.String).id(22, 8356244003862464753L);
        entityBuilder.property("examinations", PropertyType.String).id(23, 2935172316605048581L);
        entityBuilder.property("root", PropertyType.String).id(24, 3825715397976519444L);
        entityBuilder.property("tense", PropertyType.String).id(25, 8609025634979853755L);
        entityBuilder.property("type", PropertyType.String).id(26, 6622587531338528559L);
        entityBuilder.property("visit_times", PropertyType.Int).id(9, 8288004112566859257L)
                .flags(PropertyFlags.NON_PRIMITIVE_TYPE);
        entityBuilder.property("speak_speed", PropertyType.Int).id(10, 1811036805093300432L)
                .flags(PropertyFlags.NON_PRIMITIVE_TYPE);
        entityBuilder.property("backup1", PropertyType.String).id(11, 2932319709700942493L);
        entityBuilder.property("backup2", PropertyType.String).id(12, 1640133606806477691L);
        entityBuilder.property("backup3", PropertyType.String).id(13, 5900058724758816266L);


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

    private static void buildEntityCollectedData(ModelBuilder modelBuilder) {
        EntityBuilder entityBuilder = modelBuilder.entity("CollectedData");
        entityBuilder.id(14, 3960141952018617754L).lastPropertyId(5, 4475057115962061318L);

        entityBuilder.property("id", PropertyType.Long).id(1, 4883505936011134605L)
                .flags(PropertyFlags.ID);
        entityBuilder.property("objectId", PropertyType.String).id(2, 4758640470848442734L)
                .flags(PropertyFlags.INDEX_HASH).indexId(13, 7088497524958009537L);
        entityBuilder.property("name", PropertyType.String).id(3, 4153548220455600934L);
        entityBuilder.property("json", PropertyType.String).id(4, 2535851677011220860L);
        entityBuilder.property("type", PropertyType.String).id(5, 4475057115962061318L);


        entityBuilder.entityDone();
    }

    private static void buildEntityCourseList(ModelBuilder modelBuilder) {
        EntityBuilder entityBuilder = modelBuilder.entity("CourseList");
        entityBuilder.id(15, 1683819082702328425L).lastPropertyId(20, 5419052090458732126L);

        entityBuilder.property("id", PropertyType.Long).id(1, 1016975024758746046L)
                .flags(PropertyFlags.ID);
        entityBuilder.property("objectId", PropertyType.String).id(2, 2969043471136807754L)
                .flags(PropertyFlags.INDEX_HASH).indexId(14, 5394378365453439847L);
        entityBuilder.property("course_id", PropertyType.String).id(3, 1870829277024990999L)
                .flags(PropertyFlags.INDEX_HASH).indexId(15, 1910611769934093760L);
        entityBuilder.property("name", PropertyType.String).id(4, 2851468010113136622L);
        entityBuilder.property("course_num", PropertyType.Int).id(5, 4128973353364291614L)
                .flags(PropertyFlags.NOT_NULL);
        entityBuilder.property("current", PropertyType.Int).id(6, 1006698674056487381L)
                .flags(PropertyFlags.NOT_NULL);
        entityBuilder.property("order", PropertyType.Int).id(7, 3584090652438890472L)
                .flags(PropertyFlags.NOT_NULL);
        entityBuilder.property("to_activity", PropertyType.String).id(13, 659880399132415744L);
        entityBuilder.property("img", PropertyType.String).id(8, 7182746462867212593L);
        entityBuilder.property("level", PropertyType.String).id(9, 8883136987446062145L);
        entityBuilder.property("lock", PropertyType.String).id(20, 5419052090458732126L);
        entityBuilder.property("backkup", PropertyType.String).id(14, 5889711468632492630L);
        entityBuilder.property("backkup1", PropertyType.String).id(15, 6387966178170537421L);
        entityBuilder.property("backkup2", PropertyType.String).id(16, 8142938807205000063L);
        entityBuilder.property("backkup3", PropertyType.String).id(17, 7665626738488650587L);
        entityBuilder.property("backkup4", PropertyType.String).id(18, 342354979166189757L);
        entityBuilder.property("backkup5", PropertyType.String).id(19, 7672933854065894048L);
        entityBuilder.property("level_num", PropertyType.Int).id(10, 777720717726572187L)
                .flags(PropertyFlags.NOT_NULL);
        entityBuilder.property("unit_num", PropertyType.Int).id(11, 1614118075978026481L)
                .flags(PropertyFlags.NOT_NULL);
        entityBuilder.property("views", PropertyType.Int).id(12, 6289185321763757091L)
                .flags(PropertyFlags.NOT_NULL);


        entityBuilder.entityDone();
    }


}
