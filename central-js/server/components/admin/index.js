var NodeCache = require("node-cache" );
//var uuid = require("uuid");
var crypto = require('crypto');

var adminKeysCache = new NodeCache();
var cacheKey = 'admin-keys-map';
// ��� �������
var aAdminInn = [
	'3119325858'
	,'2943209693' //Белявцев
	,'3167410996' //Забрудский
        ,'2817305057' //Свидрань
        ,'3075311805' //Грек
        ,'3314612661' //Войт
        ,'2268819154' //Братусь Михаил Владимирович оператор ФО
        ,'1806101517' //Конных Юрий Николаевич оператор ФО
        ,'2007813996' //Шипов Александр Юрьевич оператор ФО
        ,'2181117011' //Тарасенко Александр Иванович оператор ФО
        ,'2451100643' //Дручкова Жанна Іванівна оператор ФО
        ,'2955913663' //Рыбкина Екатерина Николаевна Колл-центр
        ,'3017805029' //Аскерова Аида Александровна Колл-центр
        ,'3410000000' //Проскурова Олеся Олеговна Колл-центр-удален
        ,'3375301375' //Чепурко Сергей Сергеевич Колл-центр
        ,'3453103770' //Кордин Виталий Юрьевич Колл-центр
        ,'3384604904' //Левченко Юлия Вадимовна Колл-центр
        ,'3507906677' //Гавриш Олег Анатольевич Колл-центр
        ,'3318701324' //ФО Малолетнева Ульяна Евгеньевна
        ,'2819413260' //ФО Алексеенкова Анастасия Владимировна
        ,'3432314483' //БА Федько Анастасия Сергеевна оператор БО
        ,'3100218921' //БА Магоня Ирина Александровна оператор БО
        ,'3007605949' //БА Зимовец Полина Владимировна оператор БО
        ,'3118718406' //БА Салимова Лейли Александровна оператор БО
        ,'3347002733' //БА Божко Владимир Сергеевич супервизор
        ,'3233100559' //Горбань
        ,'3274107439' //Реутов Сергій Валерійович Загран КрРог
        ,'2490717289' //Бурлака Валентина Вячеславівна Загран КрРог
        ,'2701508303' //Бура Ганна Вячеславівна Загран КрРог
        ,'3227221389' //Легкошерст Ю.С. Загран КрРог
        ,'2745115749' //Людмила Довгинцівський РВ Загран КрРог
        ,'3354912955' //Дмитро Олегович Загран Кременець
        ,'3145719001' //Шмига Тетяна Віталіївна Загран Кременець
        ,'3298407213' //Невстокай Олександр Сергійович оператор БО
        ,'2506700205' //Соколова Ирина Георгиевна оператор БО
        ,'3303004002' //Мельничук Анжелика Игоревна оператор БО		
        ,'3504302600' //Леник Елена Сергеевна колл-центр
        ,'3331201403' //Гуменюк Елена Сергеевна колл-центр
        ,'3382603546' //Самойленко Мария Леонидовна колл-центр
        ,'3253714017' //Дубровец Дмитрий Владимирович колл-центр
        ,'3489209489' //Третьяк Екатерина колл-центр
        ,'3324008718' //Кравчук Алексей колл-центр
        ,'3453807654' //Ставрат Станислав колл-центр
        ,'3413409424' //Гуляк Анастасия колл-центр
	,'3413908282' //Онбыш Екатерина Николаевна колл-центр
	,'3131622274' //Пиво Александр Владимирович колл-центр
	,'3563605200' //Белая Юлия Александровна колл-центр
	,'3385106334' //Майор Сергей Владимирович колл-центр
	,'3404814782' //Белик Яна Александровна колл-центр
        ,'3340312504' //Романькова Юлия Валериевна оператор ФО
        ,'3199006657' //Горб Игорь Валентинович оператор ФО
        ,'3139206241' //Голубь Ольга Вячеславовна оператор ФО
        ,'2794700544' //Момот Ольга Вячеславовна оператор ФО
        ,'3318210825' //Донець Олена Миколаївна оператор ФО
        ,'2947005183' //Гапоненко Ольга Вікторовна оператор ФО
        ,'3459105503' //Кабанова Юлиана Юрьевна колл-центр
        ,'3456105907' //Бокарева Анастасия Геннадьевна колл-центр
        ,'3270407642' //Тетянчук Алина Сергеевна колл-центр
        ,'3428100125' //Турковец Лидия Юрьевна колл-центр
        ,'3002518216' //Вовк Евгений Викторович колл-центр
        ,'3385507197' //Воронiн Олег Юрiйович колл-центр
        ,'3358515670' //Бруяка Андрей Николаевич колл-центр
        ,'2622113164' //Мина Наталья Германовна колл-центр
        ,'3377510065' //Солнцева Анна Николаевна колл-центр
        ,'3156100721' //Романова Татьяна Ивановна колл-центр
        ,'3338000515' //Сычев Андрей колл-центр
        ,'3440714805' //София Данилевич БА Тернополь
        ,'3221220890' //Яковлев Максим загран КрРог
        ,'3366802117' //Караблин Александр Дмитриевич оператор ФО
        ,'3538107120' //Юдушкина Надежда Юрьевна оператор ФО
        ,'3300820990' //Босов Алексей Александрович оператор ФО
        ,'3325810519' //Кандыбин Александр Олегович оператор ФО
        ,'3379712465' //Шелкович Анна Игоревна оператор ФО
        ,'3497808557' //Хорунжий Яков Богданович оператор ФО
        ,'3379716566' //Алсуфьева Ольга Витальевна оператор ФО
        ,'3500101529' //Кравченко Юлия Александровна оператор ФО
        ,'3475910267' //Филиппова Анастасия Юрьевна колл-центр
        ,"3290203582" //Лещенко Оксана Сергеевна оператор ФО
        ,"3345318071" //Подопригора Дмитрий Андреевич колл-центр
        ,"3409307027" //СИМОН АЛЕКСАНДРА СЕРГЕЕВНА ДМС ДнепрРайон
        ,"2811011402" //УСЕНКО ВАЛЕНТИНА НИКОЛАЕВНА ДМС ДнепрРайон
        ,"2612400336" //Август
        ,"2520400407" //Перебора Елена Николаевна оператор ФО
        ,"2209413811" //Яланський Алім Олександрович оператор ФО
        ,"3494104123" //Вискворцова Світлана Юріївна оператор ФО
        ,"2831100257" //Смоктий Кирилл Викторович БА
        ,"3214509257" //Кулиш Андрей Юрійович БА
        ,"3088623536" //Ставицький Валерій Дмитрович БА
        ,"3225017361" //Продан Юлия Георгиевна БА
        ,"3107215745" //Столбова Анна Юрьевна БА
        ,"3101717771" //Жиган Роман Сергеевич БА
        ,"2541900347" //Гермаш Анжела Володимирівна ЗАГС
        ,"3049907728" //Чигринець Юлія Юріївна ЗАГС
        ,"2249500648" //Хоменко Наталія Миколаївна ЗАГС
        ,"2778600188" //Граб Наталя Вікторівна ЗАГС
        ,"2539216269" //Кіча Оксана Семенівна ЗАГС
        ,"3332702409" //Шапіро Яна Андріївна ЗАГС
        ,"2664017580" //Сагайдак Валерія Геннадіївна ЗАГС
        ,"3274415665" //Мартиросян Яна Робертівна ЗАГС
        ,"3443403546" //Шоломіцька Катерина Олександрівна ЗАГС
        ,"2650822029" //Пряхіна Оксана Юріївна ЗАГС
        ,"3189309384" //Ляпченко Любов Олексіївна ЗАГС
        ,"2309401607" //Цяпало Наталія Михайлівна ЗАГС
        ,"3017805509" //Сидоренко Катерина Геннадіївна ЗАГС
        ,"2895119507" //Шеян Ганна Олександрівна ЗАГС
        ,"3273818166" //Слюсаренко Ірина Петрівна ЗАГС
        ,"2963801608" //Жилка Тетяна Павлівна ЗАГС
        ,"3139808662" //Бордюг Ганна Сергіївна ЗАГС
        ,"3099807585" //Ніколаєнко Ольга Миколаївна ЗАГС 
        ,"3203800421" //Носова Ганна Анатоліївна ЗАГС
        ,"3191819764" //Дацько Катерина Сергіївна ЗАГС
        ,"2954217947" //Євтушенко Леся Федорівна Психлікарня
        ,"3171200740" //Слободян Леся Антонівна Психлікарня
        ,"3413409084" //Павленко Алина Викторовна колл-центр
];

var getAdminKeys = function () {
	var result = adminKeysCache.get(cacheKey);
	if (!result) {
		result = {};
		setAdminKeys(result);
	}
	return result;
};

var setAdminKeys = function (value) {
	adminKeysCache.set(cacheKey, value);
};

var generateAdminToken = function (inn) {
	var unhashed = inn + (new Date()).toString();
	var result = crypto.createHash('sha1').update(unhashed).digest('hex'); //uuid.v1();
	var keys = getAdminKeys();
	keys[inn] = result;
	setAdminKeys(keys);
	return result;
};

var isAdminInn = function(inn) {
	return aAdminInn.indexOf(inn) > -1;
};

var Admin = function() {

};

Admin.generateAdminToken = generateAdminToken;
Admin.isAdminInn = isAdminInn;

module.exports = Admin;
