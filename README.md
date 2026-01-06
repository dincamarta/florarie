# Aplicatie Web Florarie

**Autor:** Dinca (Mateas) Marta  
**Versiune:** 1.0  
**Data:** Ianuarie 2026

## Cuprins

1. [Specificare Functionalitati](#1-specificare-functionalitati)
2. [Descrierea Claselor si Componentelor](#2-descrierea-claselor-si-componentelor)
3. [Interfata Grafica cu Utilizatorul](#3-interfata-grafica-cu-utilizatorul)
4. [Testarea Aplicatiei](#4-testarea-aplicatiei)
5. [Imbunatatiri Viitoare](#5-imbunatatiri-viitoare)

---

## 1. Specificare Functionalitati

### 1.1 Functionalitati de Baza

#### Autentificare si Autorizare
- **Inregistrare utilizatori**: Crearea de conturi noi cu validare email si parola
- **Autentificare**: Login cu email si parola folosind Spring Security
- **Roluri**: Sistem de roluri (USER si ADMIN) cu permisiuni diferentiate
- **Securizare**: Protejarea rutelor si resurselor pe baza de rol

#### Gestionarea Catalogului de Flori
- **Vizualizare catalog**: Afisarea tuturor florilor disponibile cu detalii (nume, culoare, pret, stoc)
- **Upload imagini**: Posibilitatea de a incarca imagini pentru fiecare floare
- **Status disponibilitate**: Marcarea florilor ca disponibile sau indisponibile

#### Cosul de Cumparaturi (Buchet)
- **Adaugare produse**: Adaugarea florilor in cos cu selectarea cantitatii
- **Modificare cantitate**: Actualizarea cantitatii produselor din cos
- **Stergere produse**: Eliminarea produselor din cos cu confirmare
- **Calcul total**: Calculul automat al totalului comenzii
- **Validare stoc**: Verificarea disponibilitatii stocului la adaugare

#### Plasarea Comenzilor
- **Formular checkout**: Completarea detaliilor de livrare (adresa, telefon, deadline)
- **Observatii**: Posibilitatea de a adauga note personalizate
- **Validare date**: Validare server-side a tuturor campurilor
- **Confirmare comanda**: Salvarea comenzii in baza de date cu status NEW

#### Gestionarea Comenzilor (Utilizatori)
- **Vizualizare comenzi**: Lista tuturor comenzilor plasate de utilizator
- **Detalii comanda**: Vizualizarea detaliata a fiecarei comenzi
- **Afisare status**: Vizualizarea status-ului curent al comenzii
- **Stergere comanda**: Anularea comenzilor proprii cu confirmare

### 1.2 Functionalitati Suplimentare

#### Panou Administrativ - Gestionare Flori
- **CRUD complet**: Creare, citire, actualizare si stergere flori
- **Validare avansata**: Validari pentru nume, culoare, pret si stoc
- **Upload imagini**: Gestionarea fisierelor imagine pentru fiecare floare
- **Filtrare si cautare**: Navigare usoara in catalogul de flori

#### Panou Administrativ - Gestionare Comenzi
- **Vizualizare toate comenzile**: Acces la toate comenzile din sistem
- **Filtrare dupa status**: Filtrare comenzi dupa status (NEW, DELIVERED, IN_PROGRESS, READY, CANCELED)
- **Actualizare status**: Modificarea status-ului comenzilor
- **Comenzi intarziate**: Lista separata a comenzilor care depasesc deadline-ul
- **Stergere comenzi**: Anularea comenzilor cu confirmare

#### Sistem de Planificare Comenzi
- **Calcul etape**: Calcularea automata a duratelor etapelor (pregatire, asamblare, ambalare)
- **Verificare deadline**: Validarea daca comanda poate fi finalizata in termenul dorit
- **Avertizari**: Notificari daca etapele nu incap in ziua deadline-ului
- **Gestionare pasi**: Sistem de checklist pentru urmarea progresului comenzii

#### Sistem de Pasi pentru Comenzi
- **Initializare pasi**: Crearea automata a pasilor pentru o comanda
- **Marcare finalizare**: Toggle pentru marcarea pasilor ca finalizati
- **Ordonare pasi**: Sortare automata a pasilor dupa ordinea de lucru
- **Vizualizare progres**: Afisarea vizuala a progresului comenzii

#### Securitate si Validari
- **Validare email custom**: Validator personalizat pentru adrese de email
- **Protectie CSRF**: Protectie impotriva atacurilor Cross-Site Request Forgery
- **Hash parole**: Criptarea parolelor cu BCrypt
- **Validari campuri**: Validari complexe pe toate campurile de input

#### Seeding Date
- **Date initiale**: Popularea automata a bazei de date cu flori si utilizatori
- **Cont admin**: Crearea automata a unui cont de administrator
- **Date de test**: Flori predefinite pentru testare rapida

---

## 2. Descrierea Claselor si Componentelor

### 2.1 Pachetul Model

#### Flower.java
Clasa entitate pentru modelarea florilor din catalog.

**Atribute:**
- `id` (Long): Identificator unic generat automat
- `name` (String): Numele florii, validat cu @NotBlank, @Size si @Pattern
- `color` (String): Culoarea florii, validata similar cu numele
- `price` (Double): Pretul florii, validat cu @NotNull si @Positive
- `stock` (Integer): Cantitatea in stoc, validata cu @Min(0)
- `available` (boolean): Status de disponibilitate, implicit true
- `imagePath` (String): Calea relativa catre imaginea florii

**Validari:**
- Nume: 2-200 caractere, doar litere, cifre si punct
- Culoare: maxim 50 caractere, doar litere, cifre si cratima
- Pret: obligatoriu si pozitiv
- Stoc: obligatoriu si non-negativ

#### CustomerOrder.java
Clasa entitate pentru modelarea comenzilor plasate de clienti.

**Atribute:**
- `id` (Long): Identificator unic
- `createdAt` (LocalDateTime): Data si ora crearii comenzii
- `status` (OrderStatus): Status-ul curent al comenzii (enum)
- `requiredBy` (LocalDateTime): Deadline-ul dorit de client
- `deliveryAddress` (String): Adresa de livrare, max 200 caractere
- `phone` (String): Numar de telefon, max 30 caractere
- `notes` (String): Observatii optionale, max 500 caractere
- `total` (Double): Totalul comenzii in RON
- `user` (AppUser): Relatia Many-to-One catre utilizator
- `items` (List<OrderItem>): Lista de produse, relatie One-to-Many
- `steps` (List<OrderStep>): Lista de pasi, relatie One-to-Many

**Metode:**
- `addItem(OrderItem item)`: Adauga un produs si seteaza relatia bidirectionala
- `addStep(OrderStep step)`: Adauga un pas si seteaza relatia bidirectionala

#### AppUser.java
Clasa entitate pentru modelarea utilizatorilor aplicatiei.

**Atribute:**
- `id` (Long): Identificator unic
- `email` (String): Email unic, validat cu @Email si @NotBlank
- `passwordHash` (String): Hash-ul parolei criptat cu BCrypt
- `role` (Role): Rolul utilizatorului (USER sau ADMIN)

**Constrangeri:**
- Email unic in sistem
- Parola criptata, nu se stocheaza in plain text

#### OrderItem.java
Clasa entitate pentru modelarea unui produs dintr-o comanda.

**Atribute:**
- `id` (Long): Identificator unic
- `order` (CustomerOrder): Relatia Many-to-One catre comanda
- `flowerId` (Long): ID-ul florii comandate
- `flowerName` (String): Numele florii (copiat la momentul comenzii)
- `unitPrice` (Double): Pretul unitar (copiat la momentul comenzii)
- `quantity` (Integer): Cantitatea comandata
- `lineTotal` (Double): Totalul liniei (unitPrice * quantity)

#### OrderStep.java
Clasa entitate pentru modelarea unui pas din procesul de executare a comenzii.

**Atribute:**
- `id` (Long): Identificator unic
- `name` (String): Numele pasului (ex: "Pregatire flori")
- `done` (boolean): Status de finalizare
- `sortOrder` (Integer): Ordinea de afisare
- `order` (CustomerOrder): Relatia Many-to-One catre comanda

#### OrderStatus.java
Enum pentru status-urile posibile ale unei comenzi.

**Valori:**
- `NEW`: Comanda noua, tocmai plasata
- `DELIVERED`: Comanda preluata de administrator
- `IN_PROGRESS`: Comanda in curs de executare
- `READY`: Comanda gata pentru livrare/ridicare
- `CANCELED`: Comanda anulata

#### Role.java
Enum pentru rolurile utilizatorilor.

**Valori:**
- `USER`: Utilizator standard (client)
- `ADMIN`: Administrator cu acces complet

### 2.2 Pachetul Repository

Toate repository-urile extind `JpaRepository` si ofera metode CRUD automate.

#### FlowerRepository
Metode personalizate:
- `findByAvailableTrue()`: Returneaza doar florile disponibile

#### OrderRepository
Metode personalizate:
- `findByUserOrderByCreatedAtDesc(AppUser user)`: Comenzile unui utilizator
- `findByStatus(OrderStatus status, Sort sort)`: Filtrare dupa status
- `findOverdue(LocalDateTime now, List<OrderStatus> closed)`: Comenzi intarziate

#### UserRepository
Metode personalizate:
- `findByEmail(String email)`: Gaseste utilizator dupa email
- `existsByEmail(String email)`: Verifica existenta email-ului

#### OrderStepRepository
Metode personalizate:
- `findByOrderIdOrderBySortOrderAsc(Long orderId)`: Pasii unei comenzi, sortati
- `existsByOrderId(Long orderId)`: Verifica daca exista pasi pentru o comanda

### 2.3 Pachetul Service

#### OrderPlanService
Serviciu pentru calcularea planificarii etapelor unei comenzi.

**Metode:**
- `compute(CustomerOrder order)`: Calculeaza planul de executie

**Record-uri:**
- `StepTimes`: Contine startAt, dueAt si duration pentru un pas
- `PlanResult`: Contine harta de timpi, flag daca incape in deadline si warning

**Logica:**
- Calculeaza durata fiecarui pas bazat pe numarul de flori:
  - Pregatire: 10 secunde per floare
  - Asamblare: 5 secunde per floare
  - Ambalare: 3 minute fix
- Calculeaza inapoi de la deadline
- Verifica daca toti pasii incap in aceeasi zi cu deadline-ul

#### DbUserDetailsService
Serviciu pentru integrarea cu Spring Security.

**Metode:**
- `loadUserByUsername(String email)`: Incarca detaliile utilizatorului pentru autentificare

**Functionalitate:**
- Implementeaza `UserDetailsService` din Spring Security
- Mapeaza AppUser la UserDetails
- Converteste rolul in GrantedAuthority

### 2.4 Pachetul Controller

#### FlowerController
Controller pentru afisarea catalogului de flori (zona publica/user).

**Endpoints:**
- `GET /flowers`: Afiseaza lista de flori disponibile

#### BouquetController
Controller pentru gestionarea cosului de cumparaturi.

**Endpoints:**
- `GET /bouquet`: Afiseaza continutul cosului
- `POST /bouquet/add`: Adauga o floare in cos
- `POST /bouquet/update/{id}`: Actualizeaza cantitatea unui produs
- `GET /bouquet/remove/{id}`: Afiseaza confirmare pentru stergere
- `POST /bouquet/remove/{id}`: Sterge produsul din cos

**Gestionare sesiune:**
- Cosul este stocat in sesiunea HTTP
- Structura: Map<Long, BouquetItem> unde Long este flowerId

#### CheckoutController
Controller pentru procesul de finalizare a comenzii.

**Endpoints:**
- `GET /checkout`: Afiseaza formularul de checkout
- `POST /checkout`: Proceseaza comanda

**Validari:**
- Verifica ca buchetul nu este gol
- Valideaza toate campurile formularului
- Verifica stocul disponibil pentru fiecare produs
- Actualizeaza stocul dupa plasarea comenzii

#### OrdersController
Controller pentru gestionarea comenzilor utilizatorului.

**Endpoints:**
- `GET /orders`: Lista comenzilor utilizatorului autentificat
- `GET /orders/{id}`: Detalii comanda specifica
- `GET /orders/{id}/delete`: Confirmare stergere comanda
- `POST /orders/{id}/delete`: Sterge comanda

**Securitate:**
- Verifica ca utilizatorul poate accesa doar propriile comenzi

#### AuthController
Controller pentru autentificare si inregistrare.

**Endpoints:**
- `GET /login`: Afiseaza formularul de login
- `GET /register`: Afiseaza formularul de inregistrare
- `POST /register`: Proceseaza inregistrarea

**Functionalitate:**
- Validare formular de inregistrare
- Verificare email duplicat
- Criptare parola cu BCrypt
- Creare cont nou cu rol USER

#### AdminFlowerController
Controller pentru administrarea florilor.

**Endpoints:**
- `GET /admin/flowers`: Lista tuturor florilor
- `GET /admin/flowers/add`: Formular adaugare floare
- `POST /admin/flowers/add`: Proceseaza adaugarea
- `GET /admin/flowers/edit/{id}`: Formular editare floare
- `POST /admin/flowers/edit/{id}`: Proceseaza editarea
- `GET /admin/flowers/delete/{id}`: Confirmare stergere
- `POST /admin/flowers/delete/{id}`: Sterge floarea

**Gestionare fisiere:**
- Upload imagini in directorul `/uploads`
- Validare extensii permise (jpg, jpeg, png, gif, avif)
- Generare nume unice pentru fisiere

#### AdminOrderController
Controller pentru administrarea comenzilor.

**Endpoints:**
- `GET /admin/orders`: Lista tuturor comenzilor cu filtrare dupa status
- `GET /admin/orders/{id}`: Detalii comanda cu planificare
- `POST /admin/orders/{id}/status`: Actualizeaza status-ul comenzii
- `GET /admin/orders/overdue`: Lista comenzilor intarziate
- `POST /admin/orders/{id}/steps/{stepId}/toggle`: Marcheaza pas ca finalizat/nefinalizat
- `POST /admin/orders/{id}/steps/init`: Initializeaza pasii unei comenzi
- `GET /admin/orders/{id}/delete`: Confirmare stergere
- `POST /admin/orders/{id}/delete`: Sterge comanda
- `POST /admin/orders/{id}/deadline`: Actualizeaza deadline-ul

**Functionalitati avansate:**
- Calcul plan de executie folosind OrderPlanService
- Detectare comenzi intarziate
- Filtrare comenzi dupa status

#### HomeController
Controller pentru pagina principala.

**Endpoints:**
- `GET /`: Redirecteaza catre `/flowers`

### 2.5 Pachetul DTO (Data Transfer Objects)

#### BouquetItem
DTO pentru reprezentarea unui produs in cosul de cumparaturi.

**Atribute:**
- `flowerId` (Long): ID-ul florii
- `name` (String): Numele florii
- `price` (Double): Pretul unitar
- `quantity` (int): Cantitatea selectata
- `lineTotal` (double): Calculat automat (price * quantity)

#### CheckoutForm
DTO pentru formularul de checkout.

**Atribute:**
- `deliveryAddress` (String): Validat @NotBlank, @Size(max=200)
- `phone` (String): Validat @NotBlank, @Pattern (format telefon)
- `requiredBy` (LocalDateTime): Validat @NotNull, @Future
- `notes` (String): Optional, @Size(max=500)

#### RegisterForm
DTO pentru formularul de inregistrare.

**Atribute:**
- `email` (String): Validat @NotBlank, @ValidEmail (custom)
- `password` (String): Validat @NotBlank, @Size(min=6)
- `confirmPassword` (String): Trebuie sa fie identic cu password

**Validare custom:**
- Verifica ca password si confirmPassword sunt identice

### 2.6 Pachetul Config

#### SecurityConfig
Configurarea Spring Security.

**Functionalitati:**
- Configureaza rutele publice: `/`, `/flowers`, `/login`, `/register`, `/css/**`, `/uploads/**`
- Configureaza rutele protejate pentru ADMIN: `/admin/**`
- Configureaza rutele protejate pentru USER: `/bouquet/**`, `/checkout/**`, `/orders/**`
- Configureaza formularul de login personalizat
- Configureaza logout

#### SecurityBeans
Bean-uri pentru securitate.

**Bean-uri:**
- `PasswordEncoder`: Bean BCryptPasswordEncoder pentru criptarea parolelor
- `AuthenticationManager`: Bean pentru gestionarea autentificarii

#### WebConfig
Configurari web generale.

**Functionalitati:**
- Configureaza servirea fisierelor statice din directorul `/uploads`
- Mapare resource handlers

#### DataSeeder / FlowerSeeder
Componente pentru popularea initiala a bazei de date.

**Functionalitati:**
- Creeaza cont admin implicit (admin@florarie.ro / admin123)
- Adauga flori predefinite in catalog
- Ruleaza doar la pornirea aplicatiei daca baza este goala

### 2.7 Pachetul Validation

#### ValidEmail
Adnotare custom pentru validarea email-urilor.

**Functionalitate:**
- Permite definirea unei validari custom pentru campul email
- Poate fi extinsa cu reguli suplimentare

#### CustomEmailValidator
Implementarea validarii custom pentru email.

**Validari:**
- Verifica ca email-ul respecta formatul standard
- Poate include verificari suplimentare (domenii permise, etc.)

---

## 3. Interfata Grafica cu Utilizatorul

Aplicatia foloseste Thymeleaf pentru randarea template-urilor HTML si CSS custom pentru stilizare.

### 3.1 Pagini Publice

#### Pagina de Autentificare (`/login`)
**Elemente:**
- Formular de login cu campuri pentru email si parola
- Buton de submit "Autentificare"
- Link catre pagina de inregistrare
- Mesaje de eroare pentru credentiale invalide
- Design cu gradient violet si card centralizat
- Iconita de utilizator in header

**Stilizare:**
- Background gradient (purple)
- Card alb centralizat cu shadow
- Campuri de input stilizate cu focus effect
- Buton cu gradient si hover effect

#### Pagina de Inregistrare (`/register`)
**Elemente:**
- Formular cu campuri: email, parola, confirmare parola
- Validari client-side si server-side
- Mesaje de eroare pentru fiecare camp
- Buton "Creeaza cont"
- Link catre pagina de login
- Design consistent cu pagina de login

**Validari afisate:**
- Email obligatoriu si valid
- Parola minim 6 caractere
- Confirmare parola trebuie sa coincida
- Email duplicat (daca exista deja)

#### Catalogul de Flori (`/flowers`)
**Elemente:**
- Hero section cu titlu si descriere
- Grid de carduri pentru fiecare floare
- Fiecare card contine:
  - Imagine floare (sau placeholder)
  - Nume si culoare
  - Pret in RON
  - Badge cu stocul disponibil
  - Formular pentru selectare cantitate
  - Buton "Adauga in buchet"
- Buton de navigare catre buchet
- Buton de logout pentru utilizatori autentificati

**Interactiuni:**
- Input numeric pentru selectarea cantitatii
- Validare ca stocul este suficient
- Adaugare rapida in cos
- Feedback vizual la hover pe carduri

**Stilizare:**
- Grid responsive (3 coloane desktop, 2 coloane tableta, 1 coloana mobil)
- Carduri cu shadow si border radius
- Gradient background pentru hero
- Culori vibrante pentru butoane
- Badge-uri colorate pentru stoc

### 3.2 Pagini Utilizator Autentificat

#### Cosul de Cumparaturi (`/bouquet`)
**Elemente:**
- Hero section cu titlu si numar de produse
- Bara de navigare cu link-uri catre catalog si comenzi
- Layout cu 2 coloane:
  - Coloana stanga: Lista produse
  - Coloana dreapta: Sumar si buton checkout
- Pentru fiecare produs:
  - Nume si pret unitar
  - Formular pentru actualizare cantitate
  - Buton "Actualizeaza"
  - Total linie
  - Buton "Sterge" cu icon
- Card sumar cu:
  - Total general
  - Buton "Finalizeaza comanda"
- Mesaje de eroare pentru stoc insuficient
- Pagina goala cu mesaj si CTA daca cosul este vid

**Interactiuni:**
- Actualizare cantitate cu validare stoc
- Stergere produs cu confirmare
- Redirect catre checkout
- Continuare cumparaturi

**Stilizare:**
- Cards cu border colorat
- Butoane colorate distinct (verde update, rosu delete)
- Layout responsive
- Animatii la hover

#### Confirmare Stergere Produs (`/bouquet/remove/{id}`)
**Elemente:**
- Modal/card centralizat cu fundal gradient
- Icon mare de warning
- Titlu "Confirma stergerea"
- Mesaj de confirmare
- Card cu preview produs:
  - Nume floare
  - Pret unitar
  - Cantitate
  - Total
- Mesaj de avertizare (actiune permanenta)
- Butoane:
  - "Inapoi la buchet" (gri)
  - "Sterge din buchet" (rosu)

**Stilizare:**
- Background gradient full-screen
- Card alb centralizat cu shadow mare
- Animatii slide-in si bounce
- Butoane mari cu gradient si shadow
- Design responsive

#### Formular Checkout (`/checkout`)
**Elemente:**
- Hero section
- Layout cu 2 coloane:
  - Coloana stanga: Formular
  - Coloana dreapta: Sumar comanda
- Campuri formular:
  - Adresa de livrare (textarea)
  - Numar de telefon (input)
  - Data si ora dorita (datetime-local)
  - Observatii (textarea optional)
- Mesaje de eroare pentru fiecare camp
- Card sumar cu:
  - Lista produse
  - Total comanda
- Butoane:
  - "Inapoi la buchet"
  - "Plaseaza comanda"

**Validari afisate:**
- Toate campurile obligatorii (except observatii)
- Adresa max 200 caractere
- Telefon format valid
- Data in viitor
- Stoc suficient

**Stilizare:**
- Layout two-column responsive
- Cards cu shadow
- Campuri stilizate cu focus effect
- Butoane cu gradient

#### Lista Comenzi Utilizator (`/orders`)
**Elemente:**
- Hero section
- Grid de carduri comenzi
- Fiecare card contine:
  - Header cu numar comanda si status
  - Informatii: data, adresa, telefon, deadline
  - Total comanda
  - Butoane: "Detalii" si "Sterge"
- Badge colorat pentru fiecare status
- Pagina goala daca nu exista comenzi

**Stilizare:**
- Grid responsive
- Badge-uri colorate distinct pentru fiecare status:
  - NEW: albastru
  - DELIVERED: portocaliu
  - IN_PROGRESS: cyan
  - READY: verde
  - CANCELED: rosu
- Cards cu hover effect

#### Detalii Comanda Utilizator (`/orders/{id}`)
**Elemente:**
- Hero section cu numar comanda
- Layout cu 2 coloane:
  - Coloana stanga:
    - Card status cu badge mare
    - Card informatii livrare (adresa, telefon, deadline, observatii)
    - Card total comanda
  - Coloana dreapta:
    - Card cu lista produse comandate
    - Pentru fiecare produs: nume, pret, cantitate, total linie
- Buton "Inapoi la comenzi"

**Stilizare:**
- Layout responsive
- Cards cu iconite
- Badge status mare si colorat
- Total evidential cu font mare si culoare verde

#### Confirmare Stergere Comanda (`/orders/{id}/delete`)
**Elemente:**
- Similar cu confirmare stergere produs
- Afiseaza detalii comanda:
  - Numar comanda
  - Status
  - Total
  - Data
- Butoane confirmare/anulare

### 3.3 Panou Administrator

#### Lista Flori Admin (`/admin/flowers`)
**Elemente:**
- Hero section "Administrare Flori"
- Bara de navigare cu:
  - Link "Adauga floare noua" (verde)
  - Link "Administrare Comenzi"
  - Link "Comenzi Intarziate"
  - Link "Home"
  - Buton "Logout"
- Tabel cu toate florile:
  - Coloane: ID, Imagine, Nume, Culoare, Pret, Stoc, Disponibil, Actiuni
  - Butoane: "Editeaza" (galben) si "Sterge" (rosu)

**Stilizare:**
- Tabel responsive
- Thumbnails pentru imagini
- Butoane colorate distinct
- Navbar sticky

#### Formular Adaugare/Editare Floare (`/admin/flowers/add`, `/admin/flowers/edit/{id}`)
**Elemente:**
- Formular centralizat in card
- Campuri:
  - Nume (text)
  - Culoare (text)
  - Pret (number)
  - Stoc (number)
  - Disponibil (checkbox)
  - Imagine (file upload)
- Mesaje de eroare pentru fiecare camp
- Preview imagine existenta (pentru editare)
- Butoane: "Salveaza" si "Anuleaza"

**Validari afisate:**
- Toate validarile din model
- Restrictii pe extensii fisiere imagine

#### Confirmare Stergere Floare (`/admin/flowers/delete/{id}`)
**Elemente:**
- Card centralizat cu confirmare
- Detalii floare: nume, culoare, pret, stoc
- Imagine floare
- Avertizare ca actiunea este permanenta
- Butoane confirmare/anulare

#### Lista Comenzi Admin (`/admin/orders`)
**Elemente:**
- Hero section "Gestionare Comenzi"
- Bara de navigare
- **Sectiune filtrare status:**
  - Titlu "Filtrare dupa status"
  - Butoane pentru fiecare status + "Toate"
  - Butonul activ este highlight
  - Culori distincte pentru fiecare status
- Grid de carduri comenzi
- Fiecare card contine:
  - Header cu ID si status
  - Email client
  - Data comanda
  - Total
  - Butoane: "Detalii" si "Sterge"

**Functionalitate filtrare:**
- Click pe "Toate" afiseaza toate comenzile
- Click pe un status filtreaza comenzile respective
- Butonul activ are culoare distincta
- URL update cu parametru status

**Stilizare:**
- Sectiune filtrare cu background alb si butoane colorate
- Grid responsive
- Cards cu gradient header
- Hover effects pe carduri

#### Detalii Comanda Admin (`/admin/orders/{id}`)
**Elemente:**
- Header cu numar comanda si status
- Grid informatii:
  - Email client
  - Telefon
  - Adresa
  - Data comanda
  - Deadline
  - Total
  - Observatii
- **Sectiune planificare:**
  - Calcul etape (pregatire, asamblare, ambalare)
  - Afisare intervale orare pentru fiecare etapa
  - Durata fiecarei etape
  - Avertizare daca nu incape in deadline
- **Sectiune pasi:**
  - Lista pasi cu checkbox-uri
  - Toggle pentru marcare finalizat/nefinalizat
  - Buton "Initializeaza pasi" daca nu exista
- **Formular actualizare status:**
  - Dropdown cu toate status-urile
  - Buton "Actualizeaza status"
- **Formular actualizare deadline:**
  - Input datetime-local
  - Buton "Actualizeaza deadline"
- Lista produse comandate
- Butoane: "Inapoi" si "Sterge comanda"

**Stilizare:**
- Layout multi-sectiune
- Cards pentru fiecare sectiune
- Culori pentru planificare (verde daca incape, rosu daca nu)
- Checkboxes custom pentru pasi
- Formulare inline

#### Lista Comenzi Intarziate (`/admin/orders/overdue`)
**Elemente:**
- Hero section cu avertizare
- Lista comenzi care au depasit deadline-ul
- Fiecare card cu detalii comanda
- Accentuare vizuala (culoare rosie)
- Link catre detalii comanda

**Stilizare:**
- Design similar cu lista comenzi
- Culori de warning/danger
- Badge-uri rosii

### 3.4 Elemente Comune

#### Stiluri CSS
Aplicatia are fisiere CSS separate pentru fiecare sectiune:
- `common.css`: Stiluri generale (reset, typography, buttons)
- `auth.css`: Stiluri pentru login si register
- `flowers.css`: Stiluri pentru catalogul de flori
- `bouquet.css`: Stiluri pentru cos si confirmare stergere
- `checkout.css`: Stiluri pentru formularul de checkout
- `orders.css`: Stiluri pentru lista si detalii comenzi
- `admin.css`: Stiluri pentru panoul administrativ

**Caracteristici generale:**
- Paleta de culori consistenta (violet/purple theme)
- Gradients pentru hero sections
- Shadow-uri pentru depth
- Border radius pentru cards
- Hover effects pe butoane si cards
- Animatii subtile (transitions)
- Design responsive cu media queries
- Iconite emoji pentru feedback vizual

#### Responsiveness
- Layout-uri grid si flexbox
- Media queries pentru tableta si mobil
- Colapsare coloane pe ecrane mici
- Butoane full-width pe mobil
- Font-size adaptiv

---

## 4. Aspecte privind Testarea Aplicatiei

Aplicatia a fost testata manual prin scenarii reale de utilizare, verificand fiecare functionalitate din perspectiva utilizatorului si a administratorului.

### 4.1 Testare Manuala

#### Checklist Functionalitati Testate
#### Checklist Functionalitati Testate
- [x] Inregistrare cu email valid/invalid
- [x] Login cu credentiale corecte/incorecte
- [x] Logout
- [x] Vizualizare catalog flori
- [x] Adaugare floare in cos cu cantitate valida/invalida
- [x] Actualizare cantitate in cos
- [x] Stergere produs din cos
- [x] Finalizare comanda cu toate campurile valide
- [x] Vizualizare comenzi proprii
- [x] Stergere comanda proprie
- [x] (Admin) CRUD flori
- [x] (Admin) Upload imagini
- [x] (Admin) Vizualizare toate comenzile
- [x] (Admin) Filtrare comenzi dupa status
- [x] (Admin) Actualizare status comenzi
- [x] (Admin) Vizualizare comenzi intarziate
- [x] (Admin) Initializare si toggle pasi
- [x] Verificare securitate (acces rute protejate)
- [x] Testare responsive pe diferite device-uri

### 4.2 Scenarii de Testare

#### Scenariul 1: Proces Complet de Cumparare
**Pasi:**
1. Inregistrare cont nou cu email valid
2. Autentificare cu contul creat
3. Navigare la catalog flori
4. Selectare cantitate si adaugare flori in cos
5. Modificare cantitati in cos
6. Finalizare checkout cu date valide
7. Verificare comanda in lista comenzilor

**Rezultat:**
- Comanda creata cu succes
- Stocul actualizat corect
- Email confirmare (daca este implementat)
- Comanda vizibila in lista utilizatorului

#### Scenariul 2: Validari si Mesaje de Eroare
**Pasi:**
1. Incercare inregistrare cu email invalid
2. Incercare login cu credentiale gresite
3. Adaugare in cos cu cantitate mai mare decat stocul
4. Checkout cu campuri lipsa sau invalide

**Rezultat:**
- Mesajele de eroare apar corect
- Validarile functioneaza pe partea de server
- Utilizatorul este informat clar despre erori
- Stocul nu se actualizeaza pentru comenzi invalide

#### Scenariul 3: Administrare Flori
**Pasi:**
1. Login ca administrator
2. Adaugare floare noua cu imagine
3. Editare detalii floare existenta
4. Stergere floare cu confirmare

**Rezultat:**
- Toate operatiunile CRUD functioneaza corect
- Imaginile se incarca si se afiseaza corect
- Modificarile se salveaza in baza de date
- Confirmarile de stergere protejeaza datele

#### Scenariul 4: Gestionare Comenzi Admin
**Pasi:**
1. Login ca administrator
2. Vizualizare toate comenzile
3. Filtrare comenzi dupa status (NEW, IN_PROGRESS, etc.)
4. Actualizare status comanda
5. Initializare pasi pentru o comanda
6. Marcare pasi ca finalizati
7. Actualizare deadline

**Rezultat:**
- Filtrarea functioneaza corect
- Status-ul se actualizeaza instant
- Pasii se creeaza si se pot marca
- Calculele de planificare sunt corecte
- Avertizarile pentru deadline apar cand este necesar

#### Scenariul 5: Securitate si Control Acces
**Pasi:**
1. Incercare acces la rute protejate fara autentificare
2. User normal incearca sa acceseze /admin
3. User incearca sa vada comenzile altui utilizator
4. Verificare logout si expirare sesiune

**Rezultat:**
- Rutele protejate redirecteaza catre login
- Utilizatorii fara rol ADMIN nu pot accesa panoul admin
- Fiecare user vede doar propriile comenzi
- CSRF protection functioneaza pe toate formularele
- Sesiunile se sting corect la logout

### 4.3 Aspecte de Securitate Verificate

- **CSRF Protection**: Activat pe toate formularele POST
- **SQL Injection**: Prevenit prin JPA/Hibernate
- **XSS Prevention**: Thymeleaf escape automat
- **Parole**: Criptate cu BCrypt, nu se stocheaza in plain text
- **Sesiuni**: Expirate corect dupa logout
- **Acces Comenzi**: Doar proprietarul poate vedea comenzile
- **Upload Fisiere**: Validare extensii permise (jpg, jpeg, png, gif, avif)

### 4.4 Testare Responsive

Aplicatia a fost testata pe urmatoarele dispozitive/rezolutii:
- **Desktop**: 1920x1080, 1366x768
- **Tableta**: iPad, 768x1024
- **Mobil**: iPhone, Android, 375x667, 414x896

**Aspecte verificate:**
- Layout-urile se adapteaza corect
- Butoanele sunt accesibile pe ecrane mici
- Imaginile se redimensioneaza corespunzator
- Textul este lizibil pe toate device-urile
- Meniurile si formularele sunt utilizabile pe mobil

### 4.5 Raportare Probleme

In timpul testarii, problemele identificate au fost documentate si corectate.

**Format raportare:**
- Titlu concis
- Pasi de reproducere
- Rezultat asteptat vs rezultat actual
- Screenshots (daca este cazul)
- Browser si versiune
- Severitate (Critical, High, Medium, Low)

**Exemple categorii bug-uri:**
- Crash/Error 500
- Validare incorecta
- UI broken
- Functionalitate lipseste
- Performance issue
- Security vulnerability

---

## 5. Imbunatatiri Viitoare

### 5.1 Sistem de Notificari prin Email

Ar fi util ca aplicatia sa trimita emailuri automate catre clienti pentru a-i tine la curent cu comenzile lor. De exemplu:
- Cand clientul plaseaza o comanda, sa primeasca un email de confirmare cu detaliile comenzii
- Cand adminul schimba statusul comenzii (de la "noua" la "preluata", "in lucru" sau "gata"), clientul sa primeasca un email cu noua stare
- Inainte de termenul limita al comenzii, sa trimita un reminder administratorului
- Daca o comanda este anulata, clientul sa fie anuntat prin email

**De ce ar fi benefic:**
- Clientii ar sti mereu in ce stadiu este comanda lor fara sa sune sau sa verifice constant site-ul
- Comunicarea cu clientii ar fi automata si eficienta
- Adminul ar putea trimite si emailuri promotionale pentru oferte sau clienti fideli

### 5.2 Coduri de Reducere si Promotii

Un sistem de promotii ar ajuta la atragerea si pastrarea clientilor. Ar include:
- Administrator ar putea crea coduri de reducere (de exemplu: "VARA2026" pentru 15% reducere)
- La checkout, clientul ar putea introduce un cod promotional care sa reduca pretul
- Reducerile ar putea fi fie procent (ex: 10%), fie suma fixa (ex: 20 RON discount)
- Sistemul ar trebui sa verifice daca codul este valid, daca nu a expirat si daca nu a fost folosit deja
- Pentru clienti fideli, ar putea exista reduceri automate (ex: la a 5-a comanda primesti 10% reducere)
- Admin ar putea vedea ce coduri sunt folosite mai mult si cat au crescut vanzarile

**De ce ar fi benefic:**
- Ar creste vanzarile prin oferte speciale
- Clientii ar fi motivati sa revina pentru reduceri
- Floraria ar putea face campanii de marketing pentru ocazii speciale (8 Martie, Valentine's Day, etc.)

### 5.3 Rapoarte si Statistici pentru Administrator

Adminul ar avea nevoie de o pagina unde sa vada statistici despre magazin si vanzari:
- O vizualizare clara cu numere importante: cate comenzi au fost azi/saptamana asta/luna asta, cat s-a vandut, care este comanda medie
- Grafice pentru a vedea evolutia vanzarilor in timp
- Lista cu cele mai vandute flori pentru a sti ce sa comande mai mult
- O vedere asupra clientilor activi
- Posibilitatea de a descarca rapoartele in format PDF sau Excel pentru evidenta contabila
- Filtre pentru a vedea statistici pe perioade diferite sau pe anumite statusuri de comenzi
- Un raport special pentru comenzile intarziate ca sa inteleaga de ce (lipsa stoc, prea multe comenzi, etc.)

**De ce ar fi benefic:**
- Administratorul ar putea lua decizii mai bune bazate pe date reale (ce flori sa comande mai mult, cand sunt perioadele aglomerate)
- Ar putea identifica probleme (de ce anumite comenzi intarzie)
- Ar avea date pentru planificare si bugetare
- Ar putea anticipa cererea pentru anumite flori

### 5.4 Review-uri si Evaluari de la Clienti

Clientii ar putea lasa pareri despre florile cumparate:
- Dupa ce o comanda este gata si livrata, clientul ar primi posibilitatea sa lase un review
- Ar putea da stele de la 1 la 5 pentru fiecare tip de floare comandat
- Optional, ar putea scrie si un comentariu text
- Review-urile ar aparea pe pagina florilor, ca alti clienti sa le vada
- Doar clientii care au cumparat cu adevarat acea floare ar putea lasa review
- Adminul ar putea modera review-urile inainte sa apara public (sa stearga spam sau mesaje nepotrivite)
- Florile cu cele mai bune review-uri ar putea avea un badge special "Cel mai apreciat"
- Ar exista optiunea de a sorta florile dupa rating

**De ce ar fi benefic:**
- Clientii noi ar avea mai multa incredere cand vad ca si altii au fost multumiti
- Floraria ar primi feedback concret despre ce flori sunt apreciate si care nu
- Review-urile bune ar ajuta la vanzari (clientii cumpara mai usor produse cu evaluari bune)
- Ar crea o comunitate si engagement mai mare

### 5.5 Plati Online cu Cardul

Momentan aplicatia probabil functioneaza cu plata cash la livrare, dar ar fi util sa aiba si plati online:
- Clientul ar putea plati direct cu cardul din aplicatie
- Integrare cu servicii de plata online cunoscute (Stripe, PayPal sau Netopia pentru Romania)
- Procesul ar fi: clientul finalizeaza comanda, este redirectionat la o pagina sigura de plata, introduce detaliile cardului, plata este procesata, apoi se intoarce la site cu confirmare
- Daca plata reuseste, comanda se salveaza. Daca esueaza, comanda nu se creeaza
- Istoric cu toate platile (reusita/esuata) pentru evidenta
- Daca o comanda este anulata si a fost platita, sistemul ar permite returnarea banilor
- Generare automata de factura/chitanta dupa plata

**De ce ar fi benefic:**
- Mai convenabil pentru clienti (nu trebuie sa aiba cash la livrare)
- Siguranta ca banii sunt primiti inainte de pregatirea comenzii
- Reducerea riscului de comenzi false sau neridicate
- Procesul financiar devine mai usor de gestionat
- Mai profesional si modern



### Stack Tehnologic
- **Framework:** Spring Boot 3.5.9
- **Java:** 21
- **Build Tool:** Maven
- **Template Engine:** Thymeleaf
- **ORM:** Spring Data JPA / Hibernate
- **Database:** H2 (development) / PostgreSQL (production)
- **Security:** Spring Security 6
- **Validation:** Jakarta Validation (Hibernate Validator)

### Dependente Principale
- spring-boot-starter-web
- spring-boot-starter-thymeleaf
- spring-boot-starter-data-jpa
- spring-boot-starter-validation
- spring-boot-starter-security
- thymeleaf-extras-springsecurity6
- h2database (runtime)

### Configurare Baza de Date
Aplicatia suporta profile Spring pentru diferite medii:
- **dev**: H2 in-memory, console activata
- **docker**: PostgreSQL containerizata
- **prod**: PostgreSQL externa

### Rulare Aplicatie

#### Cerinte Sistem
- Java 21 JDK instalat
- Maven 3.8+ (sau folosire mvnw)
- 512MB RAM minim

#### Comenzi
```bash
# Compilare
mvn clean compile

# Rulare
mvn spring-boot:run

# Sau cu Maven Wrapper (Windows)
.\mvnw.cmd spring-boot:run

# Buildare JAR
mvn clean package

# Rulare JAR
java -jar target/florarie-0.0.1-SNAPSHOT.jar
```

#### Acces Aplicatie
- URL: http://localhost:8080
- Cont Admin: admin@florarie.ro / admin123
- H2 Console (dev): http://localhost:8080/h2-console


## Concluzii

Aplicatia Florarie reprezinta o solutie completa pentru gestionarea unui magazin online de flori, oferind:
- Interfata moderna si prietenoasa pentru clienti
- Panou administrativ complet pentru gestionare
- Securitate robusta cu Spring Security
- Validari extensive pentru integritatea datelor
- Planificare automata a comenzilor
- Design responsive pentru toate device-urile

Arhitectura modulara si respectarea best practices faciliteaza extinderea si mentinerea aplicatiei. Folosirea tehnologiilor moderne (Spring Boot 3, Java 21, Thymeleaf) asigura o baza solida pentru dezvoltari viitoare.

---

**Documentatie realizata de:** Dinca (Mateas) Marta  
**Data:** Ianuarie 2026  
**Versiune README:** 1.0
