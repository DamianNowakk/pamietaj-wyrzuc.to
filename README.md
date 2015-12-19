# README #

This README would normally document whatever steps are necessary to get your application up and running.

### API ###
Login do środowiska API dla zespołu: ciecimi
Hatło: v78moUzE

Potwierdzamy możliwość korzystania z następujących API (zadeklarowanych w zgłoszeniu):

 
Gdansk_harmonogram_odb_odpadow

Gdansk_zuzyte_baterie

 


### Jak działamy? ###

3 aktywnośći

1. Główny widok z informacjami kiedy wywóz śmieci suchych i śmieci mokrych.("Ładne gui" i pobranie informacji z shared preferences)

2. Aktywność ustawień(konieczność ustawienia nazwy ulicy, ustawienie jakie powiadomienia mają przychodzić do użytkownika i jak wcześnie)
Zrobiłem(mikos).Jezeli miałby coś zmieniać to fajnie by było, gdyby ulice rozwijały się z listy. Uniknelibysmy wtedy możliwośći że użytkownik się pomylił w czasie wpisywanie nazwy. 
Nazwy shared preferances:
POWIADOMIENIA_WYSTAWKI - True lub False, określa czy kotś chce otrzymawać powiadomienia o wystawkach
POWIADOMIENIA_WYSTAWKI_CZAS - 7,6,5,4,2, określa ile dni wcześniej użytkwonik ma dostać powiadomienie o wystawce
ULICA_NUMER - liczba,numer ulicy
ULICA_NAZWA - string, nazwa ulicy
RODZAJ_ZABUDOWY - 0(jednorodzinna), 1(wielorodzinna), rodzaj zabudowy       


3. Moduł odpowiedzialny za połączenie nazwy ulicy i pobranie informacji o śmieciach mokrych oraz suchych. Zapisanie tej informacji w shared preferences.  

### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact