/*
 Source Server         : 本地mysql
 Source Server Type    : MySQL
 Source Server Version : 50735
 Source Host           : localhost:3306
 Source Schema         : book_manager

 Target Server Type    : MySQL
 Target Server Version : 50735
 File Encoding         : 65001
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for book_info
-- ----------------------------
DROP TABLE IF EXISTS `book_info`;
CREATE TABLE `book_info`  (
                              `bookId` int(11) NOT NULL AUTO_INCREMENT,
                              `bookName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                              `bookAuthor` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                              `bookPrice` decimal(10, 2) NOT NULL,
                              `bookTypeId` int(11) NOT NULL,
                              `bookDesc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '書籍描述',
                              `isBorrowed` tinyint(4) NOT NULL COMMENT '1表示借出，0表示已還',
                              `bookImg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '書籍圖片',
                              PRIMARY KEY (`bookId`) USING BTREE,
                              INDEX `fk_book_info_book_type_1`(`bookTypeId`) USING BTREE,
                              CONSTRAINT `book_info_ibfk_1` FOREIGN KEY (`bookTypeId`) REFERENCES `book_type` (`bookTypeId`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 71 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book_info
-- ----------------------------
INSERT INTO `book_info` VALUES (1, 'Java程序設計', '耿祥義', 55.50, 1, '《Java2實用教程》不僅可以作為高等院校相關專業的教材，也適合自學者及軟件開發人員參考使用。Java是一種很優秀的編程語言，具有面向對象、與平台無關、安全、穩定和多線程等特點，是目前軟件設計中極為健壯的編程語言。Java語言不僅可以用來開發大型的應用程序，而且特別適合於在Internet上應用開發，Java已成為網絡時代最重要的編程語言之一。', 0, 'http://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/img/202111112241065.jpg');
INSERT INTO `book_info` VALUES (2, '紅樓夢', '曹雪芹', 36.00, 3, '《紅樓夢》是一部百科全書式的長篇小說。以寶黛愛情悲劇為主線，以四大家族的榮辱興衰為背景，描繪出18世紀中國封建社會的方方面面。', 0, 'http://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/img/202111112140064.jpg');
INSERT INTO `book_info` VALUES (4, '西遊記', '吳承恩', 60.00, 3, '《西遊記》主要描寫的是孫悟空保唐僧西天取經，歷經九九八十一難的故事。唐僧取經是歷史上一件真實的事。大約距今一千三百多年前，即唐太宗貞觀元年（627），年僅25歲的青年和尚玄奘離開京城長安，隻身到天竺（印度）遊學。他從長安出發後，途經中亞、阿富汗、巴基斯坦，歷盡艱難險阻，最後到達了印度。他在那裡學習了兩年多，並在一次大型佛教經學辯論會任主講，受到了讚譽。', 0, 'http://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/img/202111112242308.jpg');
INSERT INTO `book_info` VALUES (5, '水滸傳', '施耐庵', 50.60, 3, '《水滸傳》是我國第一部以農民起義為題材的長篇章回小說，是我國文學史上一座巍然屹立的豐碑，也是世界文學寶庫中一顆光彩奪目的明珠。數百年來，它一直深受我國人民的喜愛，並被譯為多種文字，成為我國流傳最為廣泛的古代長篇小說之一。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637228386001水滸傳.jpg');
INSERT INTO `book_info` VALUES (12, '三國演義', '羅貫中', 42.00, 3, '《三國演義》又名《三國志演義》、《三國志通俗演義》，是我國小說史上最著名最傑出的長篇章回體歷史小說。', 0, 'http://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/img/202111112143793.jpg');
INSERT INTO `book_info` VALUES (13, '三體（全集）', '劉慈欣', 92.00, 4, '三體三部曲 (《三體》《三體Ⅱ·黑暗森林》《三體Ⅲ·死神永生》) ，原名“地球往事三部曲”，是中國著名科幻作家劉慈欣的首個長篇系列。', 0, 'http://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/img/202111112143943.jpg');
INSERT INTO `book_info` VALUES (14, '天龍八部', '金庸', 58.00, 6, '天龍八部乃金筆下的一部長篇小說，與《射鵰》，《神雕》等 幾部長篇小說一起被稱為可讀性最高的金庸小說。《天龍》的故事情節曲折，內容豐富，也曾多次被改編為電視作品。', 0, 'http://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/img/202111112143125.jpg');
INSERT INTO `book_info` VALUES (27, '明朝那些事兒', '當年明月', 399.00, 2, '國民史學讀本，持續風行十餘年，暢銷3000萬冊，全本白話正說明朝大歷史', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637228686000明朝那些事兒.jpg');
INSERT INTO `book_info` VALUES (28, '沙丘', 'Frank Herbert', 394.90, 4, '每個“不可不讀”的書單上都有《沙丘》！偉大的《沙丘》六部曲中文版初次完整出版！人類每次正視自己的渺小，都是自身的一次巨大進步。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637228758971沙丘.jpg');
INSERT INTO `book_info` VALUES (30, 'C Primer Plus', '史蒂芬·普拉達', 90.50, 1, 'C語言入門教程，C語言程序設計籍，程序員的啟蒙教材，針對C11標準庫更新', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637224588022C.jpg');
INSERT INTO `book_info` VALUES (31, '計算機網絡：自頂向下方法', 'James，F.Kurose', 73.40, 1, '以自頂向下的方式系統展現計算機網絡的原理與結構，廣受歡迎的計算機網絡教材。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637230839418計算機網絡.jpg');
INSERT INTO `book_info` VALUES (32, '圍城', '錢鍾書', 30.20, 3, '《圍城》是一幅栩栩如生的世井百態圖，人生的酸甜苦辣千般滋味均在其中得到了淋漓盡致的體現。錢鍾書先生將自己的語言天才併入極其淵博的知識，再添加上一些諷刺主義的幽默調料，以一書而定江山。《圍城》顯示給我們一個真正的聰明人是怎樣看人生，又怎樣用所有作家都必得使用的文字來表述自己的“觀”和“感”的。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637228900554圍城.jpg');
INSERT INTO `book_info` VALUES (33, '平凡的世界', '路遙', 101.80, 3, '人生路遙，但沒有白走的路；在平凡的世界裡，照樣可以活得豐富而精彩。《平凡的世界》激勵了一代又一代青年人向上向善、自強不息，產生了廣泛而深遠的社會影響。讀者從路遙身上獲取勵志的力量，正在於他的作品始終充盈著奮鬥、激揚著拚搏，這是作品的魂魄，更是他人生的真實寫照。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637228780984平凡的世界.jpg');
INSERT INTO `book_info` VALUES (34, '哈利波特', 'J.K.羅琳', 648.00, 6, '本書生動幽默，感人至深，而羅琳的創作經歷就像這個故事本身一樣令人印象深刻。與哈利·波特一樣，J.K.羅琳的內心深藏著魔法。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637228304016哈利波特.jpg');
INSERT INTO `book_info` VALUES (36, '新概念英語1', '亞歷山大', 41.85, 7, '全新的教學理念、有趣的課文內容、全面的技能訓練，提供完整、經過實踐檢驗的英語學習體系！', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637228199551新概念英語1.jpg');
INSERT INTO `book_info` VALUES (37, '新概念英語2', '亞歷山大', 47.25, 7, '全新的教學理念、有趣的課文內容、全面的技能訓練，提供完整、經過實踐檢驗的英語學習體系！', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637228195386新概念英語2.jpg');
INSERT INTO `book_info` VALUES (38, '新概念英語3', '亞歷山大', 46.50, 7, '全新的教學理念、有趣的課文內容、全面的技能訓練，提供完整、經過實踐檢驗的英語學習體系！', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637228191325新概念英語3.jpg');
INSERT INTO `book_info` VALUES (39, '新概念英語4', '亞歷山大', 45.50, 7, '全新的教學理念、有趣的課文內容、全面的技能訓練，提供完整、經過實踐檢驗的英語學習體系！', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637228187117新概念英語4.jpg');
INSERT INTO `book_info` VALUES (40, '數據結構', '嚴蔚敏', 39.80, 1, '計算機科學教材', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637227911889數據結構.jpg');
INSERT INTO `book_info` VALUES (41, '數據庫系統概論', '王珊，薩師煊', 42.00, 1, '數據庫經典教材', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637227861672數據庫.jpg');
INSERT INTO `book_info` VALUES (43, '獻給阿爾吉儂的花束', '丹尼爾·凱斯', 36.00, 4, '聲稱能改造智能的科學實驗在白老鼠阿爾吉儂身上獲得了突破性的進展，下一步急需進行人體實驗。個性和善、學習態度積極的心智障礙者查理·高登成為最佳人選。手術成功後，查理的智商從68躍升為185，然而那些從未有過的情緒和記憶也逐漸浮現。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637335107095獻給阿爾吉儂的花束.jpg');
INSERT INTO `book_info` VALUES (44, '銀河帝國1', 'Foundation', 36.50, 4, '人類蝸居在銀河系的一個小角落——太陽系，在圍繞太陽旋轉的第三顆 行星上，生 活了十多萬年之久。\n人類在這個小小的行星（他們稱之為“地球”）上，建立了兩百多個不同的行政區域（他們稱之為“國家”），直到地球上誕生了第一個會思考的機器人。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637335181577銀河帝國.jpg');
INSERT INTO `book_info` VALUES (45, '中國歷代政治得失', '錢穆', 12.00, 2, '《中國歷代政治得失》為作者的專題演講合集，分別就中國漢、唐、宋、明、清五代的政府組織、百官職權、考試監察、財經賦稅、兵役義務等種種政治制度作了提要勾玄的概觀與比照，敘述因革演變，指陳利害得失。既高屋建瓴地總括了中國歷史與政治的精要大義，又點明了近現代國人對傳統文化和精神的種種誤解。言簡意賅，語重心長，實不失為一部簡明的“中國政治制度史”。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637229238833中國歷代政治得失.jpg');
INSERT INTO `book_info` VALUES (46, '萬曆十五年', '黃仁宇', 18.00, 2, '萬曆十五年，亦即公元1587年，在西歐歷史上為西班牙艦隊全部出動征英的前一年；而在中國，這平平淡淡的一年中，發生了若干為歷史學家所易於忽視的事件。這些事件，表面看來雖似末端小節，但實質上卻是以前發生大事的癥結，也是將在以後掀起波瀾的機緣。在歷史學家黃仁宇的眼中，其間的關係因果，恰為歷史的重點，而我們的大歷史之旅，也自此開始……', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637229292783萬曆十五年.jpg');
INSERT INTO `book_info` VALUES (47, '紅星照耀中國', '埃德加·斯諾', 43.00, 2, '《紅星照耀中國》（曾譯《西行漫記》）自1937年初版以來，暢銷至今，而董樂山譯本已經是今天了解中國工農紅軍的經典讀本。本書真實記錄了斯諾自1936年6月至10月在中國西北革命根據地進行實地採訪的所見所聞，向全世界報道了中國和中國工農紅軍以及許多紅軍領袖、紅軍將領的情況。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637229348394紅星.jpg');
INSERT INTO `book_info` VALUES (48, '鹿鼎記', '金庸', 96.00, 6, '這是金庸先生最後一部小說，也是登峰造極之作，是金大俠自言最喜歡之大作。 這部小說講的是一個從小在揚州妓院長大的小孩韋小寶，他不會任何武功，卻因機緣巧合闖入了江湖，並憑其絕倫機智周旋於江湖各大幫會、皇帝、朝臣之間並奉旨遠征雲南、俄羅斯之故事，書中充滿精彩絕倒的對白及逆思考的事件，令人於捧腹之餘更進一步深思其口才與機敏。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637229559975鹿鼎記.jpg');
INSERT INTO `book_info` VALUES (49, '追風箏的人', '卡勒德·胡賽尼', 29.00, 6, '12歲的阿富汗富家少爺阿米爾與僕人哈桑情同手足。然而，在一場風箏比賽後，發生了一件悲慘不堪的事，阿米爾為自己的懦弱感到自責和痛苦，逼走了哈桑，不久，自己也跟隨父親逃往美國。\n成年後的阿米爾始終無法原諒自己當年對哈桑的背叛。為了贖罪，阿米爾再度踏上暌違二十多年的故鄉，希望能為不幸的好友盡最後一點心力，卻發現一個驚天謊言，兒時的噩夢再度重演，阿米爾該如何抉擇？', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637229535609追風箏的人.jpg');
INSERT INTO `book_info` VALUES (50, '白鹿原', '陳忠實', 39.00, 3, '在從清末民初到建國之初的半個世紀裡，一陣陣狂風掠過了白鹿原上空，而每一次的變動都震盪著它的內在結構：打亂了再恢復，恢復了再打亂，細膩地反映出白姓和鹿姓兩大家族祖孫三代的恩怨紛爭。陳忠實先生在這裡，人物的命運是縱線，百回千轉，社會歷史的演進是橫面，愈拓愈寬，傳統文化的興衰則是全書的精神主體，以至人、社會歷史、文化精神三者之間相互激盪，相互作用，共同推進了作品的時空，在我們眼前鋪開了一軸恢宏的、動態的、極富縱深感的關於我們民族靈魂的現實主義的畫卷。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637229617573白鹿原.jpg');
INSERT INTO `book_info` VALUES (51, '巨人的隕落', '肯·福萊特', 35.50, 6, '在第一次世界大戰的硝煙中，每一個邁向死亡的生命都在熱烈地生長——威爾士的礦工少年、剛失戀的美國法律系大學生、窮困潦倒的俄國兄弟、富有英俊的英格蘭伯爵，以及痴情的德國特工… 從充滿灰塵和危險的煤礦到閃閃發光的皇室宮殿，從代表著權力的走廊到愛恨糾纏的臥室，五個家族迥然不同又糾葛不斷的命運逐漸揭曉，波瀾壯闊地展現了一個我們自認為了解，但從未如此真切感受過的20世紀。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637229745368巨人的隕落.jpg');
INSERT INTO `book_info` VALUES (52, '白夜行', '東野圭吾', 29.80, 6, '“只希望能手牽手在太陽下散步”，這個象徵故事內核的絕望念想，有如一個美麗的幌子，隨著無數凌亂、壓抑、悲涼的故事片段像紀錄片一樣一一還原：沒有痴痴相思，沒有海枯石爛，只剩下一個冰冷絕望的詭計，最後一絲溫情也被完全拋棄，萬千讀者在一曲救贖罪惡的悽苦愛情中悲切動容……', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637229775159白夜行.jpg');
INSERT INTO `book_info` VALUES (53, '英語語法新思維', '張滿勝', 39.00, 7, '2003年，我受邀在《新東方英語》雜誌開闢語法專欄“英語語法新思維”。轉眼間，十多年過去了。讓我沒有想到的是，我就這樣一月一篇專欄文章堅持寫到了現在，期間從未中斷過。累積下來已寫完100多篇。這些文章深受讀者喜愛，不斷有讀者建議將這些文章結集成書出版。如今呈現在大家面前的這本《英語語法新思維——語法難點妙解》就是這些專欄文章的精選。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637229921309語法新思維.jpg');
INSERT INTO `book_info` VALUES (56, 'JavaScript高級程序設計', '尼古拉斯·澤卡斯', 99.00, 1, '本書是JavaScript 超級暢銷書的最新版。ECMAScript 5 和HTML5 在標準之爭中雙雙勝出，使大量專有實現和客戶端擴展正式進入規範，同時也為JavaScript 增添了很多適應未來發展的新特性。本書這一版除增加5 章全新內容外，其他章節也有較大幅度的增補和修訂，新內容篇幅約占三分之一。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637334053325javascript.jpg');
INSERT INTO `book_info` VALUES (57, '活著', '余華', 20.00, 3, '《活著(新版)》講述了農村人福貴悲慘的人生遭遇。福貴本是個闊少爺，可他嗜賭如命，終於賭光了家業，一貧如洗。他的父親被他活活氣死，母親則在窮困中患了重病，福貴前去求藥，卻在途中被國民黨抓去當壯丁。經過幾番波折回到家裡，才知道母親早已去世，妻子家珍含辛茹苦地養大兩個兒女。此後更加悲慘的命運一次又一次降臨到福貴身上，他的妻子、兒女和孫子相繼死去，最後只剩福貴和一頭老牛相依為命，但老人依舊活著，彷彿比往日更加灑脫與堅強。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637334100418活著.jpg');
INSERT INTO `book_info` VALUES (58, '挑戰程序設計競賽', '秋葉拓哉 /岩田陽一/北川宜稔', 79.00, 1, '世界頂級程序設計高手的經驗總結\n【ACM-ICPC全球總冠軍】巫澤俊主譯\n日本ACM-ICPC參賽者人手一冊\n本書對程序設計競賽中的基礎算法和經典問題進行了匯總，分為準備篇、初級篇、中級篇與高級篇4章。作者結合自己豐富的參賽經驗，對嚴格篩選的110 多道各類試題進行了由淺入深、由易及難的細緻講解，並介紹了許多實用技巧。每章後附有習題，供讀者練習，鞏固所學。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637334263096挑戰.jpg');
INSERT INTO `book_info` VALUES (59, '人類簡史', '尤瓦爾·赫拉利', 68.00, 2, '十萬年前，地球上至少有六種不同的人\n但今日，世界舞台為什麼只剩下了我們自己？\n從只能啃食虎狼吃剩的殘骨的猿人，到躍居食物鏈頂端的智人，\n從雪維洞穴壁上的原始人手印，到阿姆斯壯踩上月球的腳印，\n從認知革命、農業革命，到科學革命、生物科技革命，\n我們如何登上世界舞台成為萬物之靈的？', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637334357792人類簡史.jpg');
INSERT INTO `book_info` VALUES (61, '中國大歷史', '黃仁宇', 19.00, 2, '中國歷史典籍浩如煙海，常使初學者不得其門而入。作者倡導“大歷史”（macro-history），主張利用歸納法將現有史料高度壓縮，先構成一個簡明而前後連貫的納領，然後在與歐美史比較的基礎上加以研究。本書從技術的角度分析中國歷史的進程，著眼於現代型的經濟體制如何為傳統社會所不容，以及是何契機使其在中國土地上落腳。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637334601318大理市.jpg');
INSERT INTO `book_info` VALUES (62, '危機與重構', '李碧妍', 79.80, 2, '“安史之亂”無疑是中國中古史上的大事關鍵，但相對於其重要性，既往的研究卻還遠遠不夠。本書從政治地理學切入，通過對唐代後半期 最為重要的政治群體之一——藩鎮的實證性考 察，對唐帝國得以成功度過“安史之亂”這一中古史上之劇變，並在由此創發的新興的藩鎮體制下，重建其政治權威與統治力的問題，給出了一個合理的歷史解釋，為我們重新認識中古史提供了一條重要的線索。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637334747447危機與重構.jpg');
INSERT INTO `book_info` VALUES (63, '笑傲江湖', '金庸', 76.80, 6, '《笑傲江湖》是中國現代作家金庸創作的一部長篇武俠小說，1967年開始創作並連載於《明報》，1969年完成。這部小說通過敘述華山派大弟子令狐沖的江湖經歷，反映了武林各派爭霸奪權的歷程。作品沒有設置時代背景，“類似的情景可以發生在任何朝代”，折射出中國人獨特的政治鬥爭狀態，同時也表露出對鬥爭的哀嘆，具有一定的政治寓意。小說情節跌宕起伏，波譎雲詭，人物形象個性鮮明，生動可感。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637335240088笑傲江湖.jpg');
INSERT INTO `book_info` VALUES (64, '東晉門閥政治', '田餘慶', 49.00, 2, '本書以豐富的史料和周密的考證分析，對中國中古歷史中的門閥政治問題作了再探索，認為中外學者習稱的魏晉南北朝門閥政治，實際上只存在於東晉一朝；門閥政治是皇權政治在特定歷史條件下出現的變態，具有暫時性和過渡性，其存在形式是門閥士族與皇權的共治。本書不落以婚宦論門閥士族的窠臼，對中國中古政治史中的這一重要問題提供了精闢的見解，具有很高的學術價值。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637335543310東晉.jpg');
INSERT INTO `book_info` VALUES (65, '激盪三十年', '吳曉波', 32.00, 2, '儘管任何一段歷史都有它不可替代的獨特性，可是，1978年—2008年的中國，卻是最不可能重複的，在一個擁有近13億人口的大國裡，僵化的計劃經濟體制日漸瓦解了，一群小人物把中國變成了一個巨大的試驗場，它在眾口睽睽之下，以不可逆轉的姿態向商業社會轉軌……', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637335658056激盪.jpg');
INSERT INTO `book_info` VALUES (66, '百年孤獨', 'Cien años de soledad', 39.50, 3, '《百年孤獨》是魔幻現實主義文學的代表作，描寫了布恩迪亞家族七代人的傳奇故事，以及加勒比海沿岸小鎮馬孔多的百年興衰，反映了拉丁美洲一個世紀以來風雲變幻的歷史。作品融入神話傳說、民間故事、宗教典故等神秘因素，巧妙地糅合了現實與虛幻，展現出一個瑰麗的想像世界，成為20世紀最重要的經典文學巨著之一。1982年加西亞•馬爾克斯獲得諾貝爾文學獎，奠定世界級文學大師的地位，很大程度上乃是憑藉《百年孤獨》的巨大影響。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637335801222百年孤獨.jpg');
INSERT INTO `book_info` VALUES (67, '長夜難明', '紫金陳', 42.00, 6, '麥家、鸚鵡史航、馬伯庸、雷米、周浩暉都推崇的作家\n社會派懸疑推理大神紫金陳“推理之王”系列第3部', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637336029232長夜難明.jpg');
INSERT INTO `book_info` VALUES (68, '基督山伯爵', '大仲馬', 43.90, 6, '小說以法國波旁王朝和七月王朝兩大時期為背景，描寫了一個報恩復仇的故事。法老號大副唐泰斯受船長的臨終囑託，為拿破崙送了一封信，受到兩個對他嫉妒的小人的陷害，被打入死牢，獄友法里亞神甫向他傳授了各種知識，還在臨終前把一批寶藏的秘密告訴了他。他設法越獄後找到了寶藏，成為巨富。從此他化名為基督山伯爵，經過精心策劃，報答了他的恩人，懲罰了三個一心想置他於死地的仇人。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637336102114基督山伯爵.jpg');
INSERT INTO `book_info` VALUES (69, '福爾摩斯探案全集', '柯南·道爾', 53.00, 6, '最經典的群眾出版社的翻譯版本，一經出版，立即風靡成千上萬的中國人。離奇的情節，扣人的懸念，世界上最聰明的偵探，人間最詭秘的案情，福爾摩斯不但讓罪犯無處藏身，也讓你的腦細胞熱情激盪，本套書獲第一屆全國優秀外國文學圖書獎。', 0, 'https://wangpeng-imgsubmit.oss-cn-hangzhou.aliyuncs.com/BookManager/pictures/1637336151137福爾摩斯.jpg');

-- ----------------------------
-- Table structure for book_type
-- ----------------------------
DROP TABLE IF EXISTS `book_type`;
CREATE TABLE `book_type`  (
                              `bookTypeId` int(11) NOT NULL AUTO_INCREMENT,
                              `bookTypeName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                              `bookTypeDesc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '書籍類型描述',
                              PRIMARY KEY (`bookTypeId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book_type
-- ----------------------------
INSERT INTO `book_type` VALUES (1, '計算機科學', '計算機相關');
INSERT INTO `book_type` VALUES (2, '歷史', '歷史相關');
INSERT INTO `book_type` VALUES (3, '文學', '文學相關');
INSERT INTO `book_type` VALUES (4, '科幻', '科幻相關');
INSERT INTO `book_type` VALUES (6, '小說', '小說相關');
INSERT INTO `book_type` VALUES (7, '外語', '外語相關');

-- ----------------------------
-- Table structure for borrow
-- ----------------------------
DROP TABLE IF EXISTS `borrow`;
CREATE TABLE `borrow`  (
                           `borrowId` int(11) NOT NULL AUTO_INCREMENT,
                           `userId` int(11) NOT NULL,
                           `bookId` int(11) NOT NULL,
                           `borrowTime` datetime NOT NULL,
                           `returnTime` datetime NULL DEFAULT NULL,
                           PRIMARY KEY (`borrowId`) USING BTREE,
                           INDEX `fk_borrow_user_1`(`userId`) USING BTREE,
                           INDEX `fk_borrow_book_info_1`(`bookId`) USING BTREE,
                           CONSTRAINT `borrow_ibfk_1` FOREIGN KEY (`bookId`) REFERENCES `book_info` (`bookId`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                           CONSTRAINT `borrow_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of borrow
-- ----------------------------
INSERT INTO `borrow` VALUES (26, 11, 2, '2021-11-18 14:24:06', '2021-11-18 16:07:03');
INSERT INTO `borrow` VALUES (27, 11, 1, '2021-11-18 15:01:31', '2021-11-18 16:07:06');
INSERT INTO `borrow` VALUES (28, 11, 4, '2021-11-18 15:22:05', '2021-11-18 16:07:08');
INSERT INTO `borrow` VALUES (30, 14, 2, '2021-11-18 16:52:05', '2021-11-19 20:55:10');
INSERT INTO `borrow` VALUES (32, 14, 4, '2021-11-18 16:52:17', '2021-11-18 16:52:41');
INSERT INTO `borrow` VALUES (38, 14, 1, '2021-11-19 22:19:43', '2021-11-19 22:19:48');
INSERT INTO `borrow` VALUES (39, 14, 1, '2021-11-19 22:46:14', '2021-11-19 22:46:18');
INSERT INTO `borrow` VALUES (40, 14, 1, '2021-11-19 22:57:21', '2021-11-19 22:57:26');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
                         `userId` int(11) NOT NULL AUTO_INCREMENT,
                         `userName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                         `userPassword` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                         `isAdmin` tinyint(4) NOT NULL COMMENT '1是管理員，0非管理員',
                         PRIMARY KEY (`userId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'admin', 1);
INSERT INTO `user` VALUES (2, '李明', '123456', 0);
INSERT INTO `user` VALUES (11, 'zhang', '123', 0);
INSERT INTO `user` VALUES (13, 'zhao', 'abc', 1);
INSERT INTO `user` VALUES (14, 'wangpeng', '123456', 0);

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for reservation_record
-- ----------------------------
DROP TABLE IF EXISTS `reservation_record`;
create table `reservation_record`
(
    `reservationId` int auto_increment
        primary key,
    `bookId`        int     not null,
    `userId`        int     not null,
    `status`        tinyint not null,
    constraint `reservation_fk_1`
        foreign key (`bookId`) references book_info (`bookId`),
    constraint reservation_fk_2
        foreign key (`userId`) references user (`userId`)
);

create index `fk_reservation_book_1`
    on `reservation_record` (`bookId`);

create index `fk_reservation_user_1`
    on `reservation_record` (`userId`);

-- ----------------------------
-- Table structure for suspension_record
-- ----------------------------
DROP TABLE IF EXISTS `suspension_record`;
create table `suspension_record`
(
    `suspensionId`       int auto_increment
        primary key,
    `userId`              int          not null,
    `borrowId`            int          not null,
    `borrowingPermission` tinyint      not null,
    `suspensionReason`    varchar(255) not null,
    `startDate`           datetime     not null,
    `endDate`             datetime     not null,
    constraint `suspension_fk_1`
        foreign key (`userId`) references user (`userId`),
    constraint suspension_fk_2
        foreign key (`borrowId`) references borrow (`borrowId`)
);

create index `fk_suspension_borrow_1`
    on `suspension_record` (`borrowId`);

create index `fk_suspension_user_1`
    on `suspension_record` (`userId`);