Applicazione Booking, (progetto personale)
Consente di prenotare le stanze di un hotel.
Ruoli:
Admin: puoi aggiungere, eliminare gli utenti, "modificarli"
HotelOwner: Possiamo aggiungere, togliere le stanze
Utente: può prenotare una stanza, visualizzare le prenotazioni effettuate, cancellare una prenotazione.

E' stato fatto per studiare spring security. Implementa attualmente un jwt token e un refresh token.

-----------------
Mancherebbe:
//TODO crsf e poi manca method level annotation
//(verificare se l'utente può fare delle determinate operazioni, verificare se la stanza appartiene a lui ecc.,
//però farlo con i method level annotation)
-spring data (dati tramite pages)
//TODO veramente si dovrebbe registrare l'utente.. non aggiungere..
-gestione concorrenza
-TODO unit tests
-eventi distribuiti
-mfa
-gestione fuso orario
-https
-risk engine


Spring:
Argomenti...:
-Spring Core
-Spring Web
-Spring Data JPA
(vedi paginazione, l'uso dello stream (non carica tutto), projection, query method, custom query e così via..)
 e altri concetti utili, fatti suggerire esercizi dall'ai magari
-Transactions
-Concorrenza + locking
-Event-driven
-Security
-Performance tuning