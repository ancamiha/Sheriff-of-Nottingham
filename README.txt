Enache Anca- Mihaela 324CD
La scheletul propus am adaugat package-ul players ce contine 8 clase.

Patru dintre clasa sunt inrudite: Player, Basic, Greedy si Bribed.
-Player este o clasa abstracta ce contine caracteristicile principale a fiecarui jucator:
id, type, strategy, cards, coins, honesty, bribe, bag si declaredCardType. De asemenea
contine getterii si setterii corespunzatori fiecarui camp, 5 metode abstracte implementate
in clasa Basic si o metoda de tip void ce reprezinta "taraba" fiecarui jucator.

-Clasa Basic mosteneste clasa Player. Are 6 metode: freqCalc, getMaxGood, getMaxProfit,
getMaxId, maleBag si whenItsSheriff. FreqCalc calculeaza frecventa cartilor legale din
pachetul de carti detinut de jucatorul cu strategia Basic si returneaza un hashMap, iar in
caz ca nu exista carti legale sorteaza cartile ilegale descrescator in functie de profit
si returneaza prima carte din lista. GetMaxGood cauta cartea cu frecventa cea mai mare,ce 
insa daca exista mai multe carti cu aceeasi frecventa va creea un hashMap cu aceste carti
ce va fi folosit la apelul metodei getMaxProfit si getMaxId. Apeland metoda getMaxProfit se
creeaza un hashMap cu cartile de profit maxim din cele cu frecventa maxima si ulterior prin
metoda getMaxId se alege id-ul cel mai mare.
Metoda makeBag realizeaza sacul jucatorului Basic tinand cont de regulile impuse eliminand
dupa alegerea unei carti cartea respectiva din pachetul original. La finalul metodei se 
declara bunul adaugat in sac, daca are doar carti legale, id-ul corespunzator, daca are o
singura carte ilegala o declara ca "mere". Se seteaza onestitatea jucatorului si mita pe 0.
Metoda whenItsSheriff cuprinde inspectia realizata de seriful Basic in functie de onestitatea
jucatorilor verificati, daca sunt sinceri bunurile sunt adaugate pe taraba.

-Clasele Greedy si Bribed mostenesc clasa Basic fiecare continand cate 2 metode: makeBag si
whenItsSheriff respectand regulile impuse de cerinta.
-Metoda makeBagGreedy apeleaza de fiecare data metoda makeBag din Basic iar in rundele pare
alege o carte ilegala de profit maxim din cele ramase in pachetul initial, daca se face acest
lucru se seteaza onestitatea jucatorul pe "Liar", iar mita ramane 0. Returneaza un ArrayList
ce contine cartile din sac.
-Metoda whenItsSheriffGreedy apeleaza metoda whenItsSheriff din Basic daca mita este egala cu
0, insa daca ea exista o ia, iar comerciantul ce a acordat mita isi pune cartile pe taraba.

-In metoda makeBagBribed se ordoneaza pachetul de carti in functie de profit si id cu 
comparatorul ProfitComparatorBribed si se verifica componenta pachetului initial de carti.
Daca nu exista nicio carte ilegala in el se apeleaza metoda makeBag din Basic. Daca nu
se adauga cartile ilegala cu profitul cel mai mare cat timp sunt maxim 8 tinand cont si 
de penalty-ul ce ar putea fi luat pe cartile respective la inspectie. Dupa ce se adauga se
verifica cate s-au adaugat in sac si se stabileste mita. Daca sunt 1 sau 2 mita va fi 5, daca
sunt mai mult de 2 mita va fi 10. Daca sacul nu s-a completat se adauga carti legale pana 
cand se umple la fel, tinandu-se cont de penalty-ul ce ar putea exista. Returneaza un ArrayList
ce contine cartile din sac.
-Metoda whenIsSheriffBribed inspecteaza mai intai jcatorul din stanga si din dreapta, iar in
caz ca seriful nu are suficienti bani vecinii acestuia isi adauga cartile pe taraba. Atunci
cand seriful ajunge la ceilalti jucatori si acestia vor sa dea mita, mita este scazuta din
banii comerciantului si bunurile puse pe masa, insa mita va fi returnata de metoda si 
adaugata intr-o variabila din Main pentru a nu afecta conditia de bani minimi ai serifului.
Daca acestia nu dau mita pur si simplu isi vor pune bunurile pe taraba.

-Clasele PlayerHonesty si PlayerType sunt de tip enum.
-Clasa ProfitComparatorBribed contine implementarea unui comparator ce are ca scop sortarea
unei liste descrescator in functie de profit, iar in caz ca profitul este identic
descrescator dupa id.
-Clasa ScoreCalculator este o clasa final ce respecta design pattern-ul Singleton. Contine
4 metode si un comparator. Una dintre metode "compute" inglobeaza apelul celorlalte si 
realizeaza clasamentul cu ajutorul comparatorului.

-Clasa main cuprinde realizarea listei de jucatori, impartirea cartilor pentru care am 
folosit o coada(queue) pentru a simplifica procesul si pentru a putea folosi metoda remove()
specifica ce intoarce si sterge elementul din varful cozii. Dupa impartirea cartilor creez
sacii(ArratList) in functie de strategia pe care o adopta comerciantul, apoi are loc 
inspectia. Actiune putin diferita la seriful Bribed ce inspecteaza jucatorii vecini lui si
intr-o variabila locala in main "sumB" pastreaza mita luata de la ceilalti jucatori, mita
ce va fi adaugata la finalul rundei la monezile serifului pentru a nu afecta conditia minima
de inspectie(15 bani). De asemenea la sfarsitul subrundei seriful devine comerciant, se 
reseteaza onestitatea fiecarui jucator si se decarteaza cartile. 
-La final se apeleaza metoda "compute" din clasa Singleton ScoreCalculator pentru a calcula
si realiza clasamentul.
-Se afiseaza pe consola.
