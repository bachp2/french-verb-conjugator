import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Created by bachp on 4/16/2017.
 */
public class SimilarRadsDict {
    /**
     * is a dictionary of verbs with similar radical
     */
    private static final Map<String, String[]> MAP;
    static{
        MAP = Maps.newHashMap();
        MAP.put("null",new String[]{"aller","avoir","ravoir","être"});
        MAP.put("pât",new String[]{"pâter","pâtir"});
        MAP.put("c",new String[]{"celer","céder"});
        MAP.put("d",new String[]{"devoir","dire"});
        MAP.put("g",new String[]{"geler","gérer","gésir"});
        MAP.put("l",new String[]{"lever","lire","lécher","léguer","léser"});
        MAP.put("m",new String[]{"mener","mettre","mourir","mouvoir","mécher","métrer"});
        MAP.put("p",new String[]{"paître","peler","peser","pouvoir","pécher","péter"});
        MAP.put("r",new String[]{"rire","régler","régner"});
        MAP.put("s",new String[]{"savoir","semer","seoir","sevrer","sourdre","sécher"});
        MAP.put("t",new String[]{"taire","tenir","téter"});
        MAP.put("v",new String[]{"vener","venir","vivre","voir","vouloir"});
        MAP.put("saur",new String[]{"saurer","saurir"});
        MAP.put("cot",new String[]{"coter","cotir"});
        MAP.put("tra",new String[]{"tracer","traire"});
        MAP.put("piqu",new String[]{"piquer","piqueter"});
        MAP.put("cur",new String[]{"curer","cureter"});
        MAP.put("cuv",new String[]{"cuveler","cuver"});
        MAP.put("cliqu",new String[]{"cliquer","cliqueter"});
        MAP.put("hourd",new String[]{"hourder","hourdir"});
        MAP.put("caill",new String[]{"cailler","cailleter"});
        MAP.put("terr",new String[]{"terrer","terrir"});
        MAP.put("brid",new String[]{"brider","bridger"});
        MAP.put("dor",new String[]{"dorer","dormir"});
        MAP.put("démur",new String[]{"démurer","démurger"});
        MAP.put("bann",new String[]{"banner","bannir"});
        MAP.put("barr",new String[]{"barrer","barrir"});
        MAP.put("décoll",new String[]{"décoller","décolleter"});
        MAP.put("louv",new String[]{"louver","louveter"});
        MAP.put("ven",new String[]{"vendre","venger"});
        MAP.put("banqu",new String[]{"banquer","banqueter"});
        MAP.put("vid",new String[]{"videler","vider"});
        MAP.put("rév",new String[]{"révéler","révérer"});
        MAP.put("vol",new String[]{"voler","voleter"});
        MAP.put("retent",new String[]{"retenter","retentir"});
        MAP.put("décarr",new String[]{"décarreler","décarrer"});
        MAP.put("grav",new String[]{"graver","gravir"});
        MAP.put("épann",new String[]{"épanneler","épanner"});
        MAP.put("rain",new String[]{"rainer","raineter"});
        MAP.put("bât",new String[]{"bâter","bâtir"});
        MAP.put("fil",new String[]{"filer","fileter"});
        MAP.put("surv",new String[]{"survenir","survivre"});
        MAP.put("surs",new String[]{"sursemer","surseoir","sursoir"});
        MAP.put("surf",new String[]{"surfaire","surfer"});
        MAP.put("surg",new String[]{"surgeler","surgir"});
        MAP.put("ch",new String[]{"choir","chérer"});
        MAP.put("cr",new String[]{"crever","croire","croître","crécher","crémer","créner"});
        MAP.put("dépar",new String[]{"déparer","départir"});
        MAP.put("aiguill",new String[]{"aiguiller","aiguilleter"});
        MAP.put("rengr",new String[]{"rengrener","rengréner"});
        MAP.put("fa",new String[]{"faillir","falloir"});
        MAP.put("fi",new String[]{"fier","figer"});
        MAP.put("fon",new String[]{"foncer","fondre"});
        MAP.put("fl",new String[]{"fleurir","flécher"});
        MAP.put("for",new String[]{"forcer","forer","forger"});
        MAP.put("fou",new String[]{"fouger","fouir","foutre"});
        MAP.put("gr",new String[]{"grener","grever"});
        MAP.put("rass",new String[]{"rasseoir","rassir","rassoir"});
        MAP.put("la",new String[]{"lacer","layer"});
        MAP.put("fus",new String[]{"fuseler","fuser"});
        MAP.put("lu",new String[]{"luger","luire"});
        MAP.put("pa",new String[]{"pager","payer"});
        MAP.put("pi",new String[]{"piger","piéger","piéter"});
        MAP.put("pl",new String[]{"plaire","pleuvoir"});
        MAP.put("ra",new String[]{"rager","raire","rayer"});
        MAP.put("su",new String[]{"sucer","suer"});
        MAP.put("embr",new String[]{"embreler","embrever"});
        MAP.put("gob",new String[]{"gober","gobeter"});
        MAP.put("fourb",new String[]{"fourber","fourbir"});
        MAP.put("marqu",new String[]{"marquer","marqueter"});
        MAP.put("écart",new String[]{"écarteler","écarter"});
        MAP.put("hal",new String[]{"haler","haleter"});
        MAP.put("hav",new String[]{"haver","havir"});
        MAP.put("chanc",new String[]{"chanceler","chancir"});
        MAP.put("atterr",new String[]{"atterrer","atterrir"});
        MAP.put("croch",new String[]{"crocher","crocheter","crochir"});
        MAP.put("écrou",new String[]{"écrouer","écrouir"});
        MAP.put("déc",new String[]{"déceler","décéder"});
        MAP.put("déf",new String[]{"défaire","déféquer","déférer"});
        MAP.put("dém",new String[]{"démener","démettre"});
        MAP.put("dét",new String[]{"dételer","détenir"});
        MAP.put("repen",new String[]{"rependre","repentir"});
        MAP.put("reper",new String[]{"repercer","reperdre"});
        MAP.put("enra",new String[]{"enrager","enrayer"});
        MAP.put("pomm",new String[]{"pommeler","pommer"});
        MAP.put("repar",new String[]{"reparaître","repartir"});
        MAP.put("accr",new String[]{"accroître","accréter"});
        MAP.put("ressu",new String[]{"ressuer","ressuyer"});
        MAP.put("établ",new String[]{"établer","établir"});
        MAP.put("détonn",new String[]{"détonneler","détonner"});
        MAP.put("jou",new String[]{"jouer","jouir"});
        MAP.put("épin",new String[]{"épincer","épiner"});
        MAP.put("él",new String[]{"élever","élire"});
        MAP.put("ém",new String[]{"émettre","émouvoir","émécher"});
        MAP.put("compar",new String[]{"comparaître","comparer"});
        MAP.put("parqu",new String[]{"parquer","parqueter"});
        MAP.put("décap",new String[]{"décapeler","décaper"});
        MAP.put("appoint",new String[]{"appointer","appointir"});
        MAP.put("brett",new String[]{"bretteler","bretter"});
        MAP.put("décri",new String[]{"décrier","décrire"});
        MAP.put("désenlaid",new String[]{"désenlaider","désenlaidir"});
        MAP.put("débât",new String[]{"débâter","débâtir"});
        MAP.put("tromp",new String[]{"tromper","trompeter"});
        MAP.put("lan",new String[]{"lancer","langer"});
        MAP.put("clav",new String[]{"claver","claveter"});
        MAP.put("clap",new String[]{"claper","clapir"});
        MAP.put("clam",new String[]{"clamecer","clamer"});
        MAP.put("chevr",new String[]{"chevrer","chevreter"});
        MAP.put("prom",new String[]{"promener","promettre","promouvoir"});
        MAP.put("louch",new String[]{"loucher","louchir"});
        MAP.put("récri",new String[]{"récrier","récrire"});
        MAP.put("about",new String[]{"abouter","aboutir"});
        MAP.put("craqu",new String[]{"craqueler","craquer","craqueter"});
        MAP.put("mod",new String[]{"modeler","modérer"});
        MAP.put("abonn",new String[]{"abonner","abonnir"});
        MAP.put("mus",new String[]{"museler","muser"});
        MAP.put("épinc",new String[]{"épinceler","épinceter"});
        MAP.put("langu",new String[]{"langueter","languir"});
        MAP.put("écor",new String[]{"écorcer","écorer"});
        MAP.put("décr",new String[]{"décroître","décréter"});
        MAP.put("écri",new String[]{"écrier","écrire"});
        MAP.put("briqu",new String[]{"briquer","briqueter"});
        MAP.put("claqu",new String[]{"claquer","claqueter"});
        MAP.put("défi",new String[]{"défier","défiger"});
        MAP.put("déla",new String[]{"délacer","délayer"});
        MAP.put("déra",new String[]{"dérager","dérayer"});
        MAP.put("coll",new String[]{"coller","colleter"});
        MAP.put("conf",new String[]{"confire","conférer"});
        MAP.put("cont",new String[]{"contenir","conter"});
        MAP.put("comm",new String[]{"commettre","commérer"});
        MAP.put("becqu",new String[]{"becquer","becqueter"});
        MAP.put("cors",new String[]{"corser","corseter"});
        MAP.put("feuill",new String[]{"feuiller","feuilleter"});
        MAP.put("cord",new String[]{"cordeler","corder"});
        MAP.put("coqu",new String[]{"coquer","coqueter"});
        MAP.put("couv",new String[]{"couver","couvrir"});
        MAP.put("par",new String[]{"paraître","parer","partir"});
        MAP.put("émouch",new String[]{"émoucher","émoucheter"});
        MAP.put("per",new String[]{"percer","percevoir","perdre"});
        MAP.put("souffl",new String[]{"souffler","souffleter"});
        MAP.put("prév",new String[]{"prévenir","prévoir"});
        MAP.put("pon",new String[]{"poncer","pondre"});
        MAP.put("afferm",new String[]{"affermer","affermir"});
        MAP.put("tach",new String[]{"tacher","tacheter"});
        MAP.put("soup",new String[]{"souper","soupeser"});
        MAP.put("compl",new String[]{"complaire","compléter"});
        MAP.put("saill",new String[]{"sailler","saillir"});
        MAP.put("ram",new String[]{"ramener","ramer"});
        MAP.put("ach",new String[]{"acheter","achever"});
        MAP.put("rec",new String[]{"receler","receper","recéder","recéper"});
        MAP.put("rel",new String[]{"relever","relire","reléguer"});
        MAP.put("ren",new String[]{"renaître","rendre"});
        MAP.put("rep",new String[]{"repaître","repérer"});
        MAP.put("épi",new String[]{"épicer","épier"});
        MAP.put("rev",new String[]{"revenir","revivre","revoir"});
        MAP.put("éta",new String[]{"étager","étayer"});
        MAP.put("riv",new String[]{"river","riveter"});
        MAP.put("dégross",new String[]{"dégrosser","dégrossir"});
        MAP.put("cach",new String[]{"cacher","cacheter"});
        MAP.put("all",new String[]{"allécher","alléger","alléguer"});
        MAP.put("ros",new String[]{"roser","rosir"});
        MAP.put("rou",new String[]{"rouer","rouir"});
        MAP.put("app",new String[]{"apparoir","appeler"});
        MAP.put("calm",new String[]{"calmer","calmir"});
        MAP.put("moit",new String[]{"moiter","moitir"});
        MAP.put("mois",new String[]{"moiser","moisir"});
        MAP.put("ass",new String[]{"assener","asseoir","assoir","assécher","asséner"});
        MAP.put("cann",new String[]{"canneler","canner"});
        MAP.put("bond",new String[]{"bonder","bondir"});
        MAP.put("fleur",new String[]{"fleurer","fleureter"});
        MAP.put("caqu",new String[]{"caquer","caqueter"});
        MAP.put("carr",new String[]{"carreler","carrer"});
        MAP.put("boss",new String[]{"bosseler","bosser"});
        MAP.put("boul",new String[]{"bouler","bouléguer"});
        MAP.put("bott",new String[]{"botteler","botter"});
        MAP.put("sal",new String[]{"saler","salir"});
        MAP.put("viol",new String[]{"violer","violeter"});
        MAP.put("bad",new String[]{"bader","badger"});
        MAP.put("bat",new String[]{"bateler","battre"});
        MAP.put("alun",new String[]{"aluner","alunir"});
        MAP.put("mouch",new String[]{"moucher","moucheter"});
        MAP.put("bouff",new String[]{"bouffer","bouffir"});
        MAP.put("effleur",new String[]{"effleurer","effleurir"});
        MAP.put("bou",new String[]{"bouger","bouillir","bouéler"});
        MAP.put("bra",new String[]{"braire","brayer"});
        MAP.put("paill",new String[]{"pailler","pailleter"});
        MAP.put("bourr",new String[]{"bourreler","bourrer"});
        MAP.put("tap",new String[]{"taper","tapir"});
        MAP.put("tar",new String[]{"tarer","tarir"});
        MAP.put("cal",new String[]{"caler","caleter"});
        MAP.put("cap",new String[]{"capeler","caper"});
    }

    /**
     *
     * @param s
     * @return
     */
    public static boolean contains(String s){
        return MAP.containsKey(s);
    }

    /**
     *
     * @param key
     * @return
     * @throws NullPointerException
     */
    public static String[] getVerbsString(String key) throws NullPointerException{
        return MAP.get(key).clone();
    }
}
