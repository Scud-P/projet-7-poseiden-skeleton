# OpenClassrooms - Parcours de développeur d'applications Java - Projet 7

## Solution choisie

### Architecture MVC

* Modèle : Le modèle est constitué des entités persistantes du domaine (BidList, CurvePoint, Rating, RuleName, Trade,
  DBUser).

* Vue : Un ensemble de pages HTML, formatées à l'aide de CSS et l'utilisation de Bootstrap. Nous utilisons aussi
  Thymeleaf pour peupler certains éléments HTML en fonction de notre modèle.

* Contrôleurs : Ils permettent de gérer les requêtes de l'utilisateur depuis la couche vue, interagissent avec la couche
  de service et mettent à jour le modèle. Ils offrent la redirection vers des endpoints spécifiques.

* Services : La couche de service est responsable de l'implémentation de la logique métier. De l'enregistrement des
  utilisateurs à la validation des données, en passant par la gestion des erreurs et de l'aspect transactionnel de
  certaines fonctionnalités.

* Repositories : La couche repository est chargée de fournir des méthodes susceptibles d'être appelées par les
  différents services afin de lire, créer et modifier les données persistées dans les différentes tables de la base de
  données.

* Base de données relationnelle : De type MySQL, elle permet de stocker dans ses tables les données nécessitant une
  persistance.

### Langages de programmation

* Backend: Java 17
* Frontend: HTML/CSS

### Stack technique

|          Fonction           |            Outil             |
|:---------------------------:|:----------------------------:|
|          Framework          | Java Spring with Spring Boot |
|      Build & Packaging      |            Maven             |
|  Data Access & Persistence  | Spring Boot Starter Data JPA |
| Autoconfiguration & Servlet |   Spring Boot Starter Web    |
|  Security & Authentication  |       Spring Security        |
|          Database           |            MySQL             |
| Boilerplate Code Reduction  |            Lombok            |
|         DTO Mapping         |          Mapstruct           |
|           Testing           |  JUnit 5, MockMvc, Mockito   |
|      Testing coverage       |            JaCoCo            |
|       Testing report        |           Surefire           |
|       Template engine       |          Thymeleaf           |
|      Frontend toolkit       |          BootStrap           |

### Approche transactionnelle et gestion des erreurs

**Service**

L'annotation `@Transactional` de Spring Boot, faisant partie du Framework Spring, simplifie la gestion du
commit/rollback des transactions. Une transaction est créée avant l'invocation de la méthode annotée, s'ensuit un commit
si aucune erreur ne se produit, et un rollback en cas d'erreur comme la levée d'une exception, garantissant ainsi
l'intégrité des données.

```
        @Transactional
    public DBUser addUser(@Nonnull DBUserParameter dbUserParameter) {

        // Check if the userName is already used by another user, we want to maintain uniqueness
        // since our CustomUserDetailsService loads users by their userName to create userDetails objects.
        
        if (isUserNameAlreadyUsed(dbUserParameter.getUsername())) {
            throw new UserNameAlreadyUsedException("userName already used");
        }
        DBUser dbUserToAdd = dbUserMapper.toDBUser(dbUserParameter);
        dbUserToAdd.setPassword(passwordEncoder.encode(dbUserToAdd.getPassword()));
        userRepository.save(dbUserToAdd);
        return dbUserToAdd;
    }
```

**Controller**

Les méthodes transactionnelles prennent en paramètre un objet BindingResult qui permet de tirer avantage des contraintes
de validation.
Lorsqu'un champ renseigné par l'utilisateur ne respecte pas ces contraintes, nous pouvons lui soumettre un feedback afin
qu'il corrige sa prochaine saisie.

Lorsque le controller appelle la méthode d'un service et que celle-ci lance une exception, elle se propage et est
attrapée au niveau du controller.
Nous utilisons ensuite BindingResult pour rejeter l'input de l'utilisateur même si cet input respecte les contraintes de
validation, dans le cas d'une exception.

```
     /**
     * Validates and saves a new user
     *
     * @param dbUserParameter the parameter object containing the user data
     * @param result         the binding result for validation errors
     * @param model          the model to which the updated list of users is added
     * @return the view name to redirect to after saving the user, or the view name for adding a user if there are validation errors
     * or the username is already used, or finally the view name for the error page in case an IllegalArgumentException is caught
     */
     
    @PostMapping("/DBUser/validate")
    public String validate(@Valid DBUserParameter dbUserParameter, BindingResult result, Model model) {

        // Check if password meets the criteria (8-20 characters, at least one lowercase and one uppercase letter,
        // at least one number and at least one special character)
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";

        if (!dbUserParameter.getPassword().matches(passwordPattern)) {
            result.rejectValue("password", "error.dbUser",
                    "Password must be 8-20 characters long, contain at least one digit," +
                            " one lowercase letter, one uppercase letter, one special character (@#$%^&+=!)" +
                            ", and have no spaces.");
        }
        if (!result.hasErrors()) {
            try {
                userService.addUser(dbUserParameter);
                model.addAttribute("dbUserDTOs", userService.getAllUsers());
                return "redirect:/DBUser/list";

                // Catches the UserNameAlreadyUsedException, we can't afford to have two users with the same userName since
                // we load grantedAuthorities based on the userName
            } catch (UserNameAlreadyUsedException usedException) {
                result.rejectValue("username", "error.dbUser", "Username already exists");
                model.addAttribute("dbUserParameter", dbUserParameter);

            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        }
        model.addAttribute("dbUserParameter", dbUserParameter);
        return "DBUser/add";
    }
```

**Vue**

Grâce à Thymeleaf, nous pouvons ensuite afficher à l'utilisateur cette information dans la vue.

```
 <form action="#" th:action="@{/DBUser/validate}" th:object="${DBUserParameter}" method="post" class="form-horizontal"
              style="width: 100%">
              ...
                <label for="username" class="col-sm-2 control-label">User Name</label>
                <div class="col-sm-10">
                    <input type="text" th:field="*{username}" id="username" placeholder="User Name" class="col-4">
                    <p class="text-danger" th:if="${#fields.hasErrors('username')}" th:errors="*{username}"></p>
                </div>
            </div>
            ...
        </form>
```

**Repository**

Si en revanche tout se passe bien lors de la transaction, la méthode du repository est appelée. L'entité créée est ainsi
persistée en base de données par les méthodes natives comme `userRepository.save(user)` de nos
repositories qui étendent JpaRepository.

```
@Repository
public interface UserRepository extends JpaRepository<DBUser, Long> {
// Autres méthodes...
}
```

## Validation des données

**Frontend**

Utilisation de règles de validation dans les templates HTML.

```input type="text"``` Utilisation d'un String en entrée 

```pattern="[A-Za-z0-9\-\+]{0,125}"```Utilisation des expressions régulières pour spécifier les caractères autorisés. 

```<p class="text-danger" th:if="${#fields.hasErrors('moodysRating')}" th:errors="*{moodysRating}"></p>```Affiche un message à l'utilisateur en cas de validation échouée.


```
            <div class="form-group">
                <label for="moodysRating" class="col-sm-2 control-label">MoodysRating</label>
                <div class="col-sm-10">
                    <input type="text" th:field="*{moodysRating}" id="moodysRating" placeholder="MoodysRating"
                           pattern="[A-Za-z0-9\-\+]{0,125}" required
                           title="Only letters (A-Z, a-z), numbers (0-9), + and - are allowed, up to 125 characters"
                           class="col-4">
                    <p class="text-danger" th:if="${#fields.hasErrors('moodysRating')}" th:errors="*{moodysRating}"></p>
                </div>
            </div>
```

**Backend**

Pour garantir la validation des données côté serveur, j'ai utilisé des annotations de validation de Jakarta Bean Validation sur les entités.
Les annotations telles que @NotBlank, @Size, @Min, et @Max assurent que les champs respectent les contraintes de validation avant d'être persistés dans la base de données.

```
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "moodysRating", length = 125)
    @NotBlank(message = "moodysRating is mandatory")
    @Size(max = 125, message = "Only 125 characters allowed")
    private String moodysRating;

    @Column(name = "sandPRating", length = 125)
    @NotBlank(message = "sandPRating is mandatory")
    @Size(max = 125, message = "Only 125 characters allowed")
    private String sandPRating;

    @Column(name = "fitchRating", length = 125)
    @NotBlank(message = "fitchRating is mandatory")
    @Size(max = 125, message = "Only 125 characters allowed")
    private String fitchRating;

    @NotNull(message = "orderNumber is mandatory")
    @Column(name = "orderNumber")
    @Min(value = 0, message = "Only positive values from 0 to 255")
    @Max(value = 255, message = "Only positive values from 0 to 255")
    private Integer orderNumber;
}
```
Si des erreurs de validation sont détectées, elles sont gérées et affichées à l'utilisateur. Cela garantit que seules les données valides sont traitées et stockées.

```
@PostMapping("/rating/validate")
public String validate(@Valid RatingParameter ratingParameter, BindingResult result, Model model) {

    if (!result.hasErrors()) {
        try {
            ratingService.addRating(ratingParameter);
            model.addAttribute("ratingDTOs", ratingService.getAllRatings());
            return "redirect:/rating/list";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }
    model.addAttribute("ratingParameter", ratingParameter);
    return "rating/add";
}
```


## Utilisation d'objets lightweight - parameter en entrée et DTO en sortie

Afin d'éviter de manipuler des entités, nous utilisons les parameters en entrée.

```
 @PostMapping("/rating/validate")
    public String validate(@Valid RatingParameter ratingParameter, BindingResult result, Model model) {

        if (!result.hasErrors()) {
            try {
                ratingService.addRating(ratingParameter);
                model.addAttribute("ratingDTOs", ratingService.getAllRatings());
                return "redirect:/rating/list";

            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        }
        model.addAttribute("ratingParameter", ratingParameter);
        return "rating/add";
    }
```

Nous pouvons ensuite utiliser la librairie MapStruct pour mapper les champs du parameter aux champs de notre entité à persister.

```
    @Transactional
    public Rating addRating(RatingParameter ratingParameter) {
        Rating rating = ratingMapper.toRating(ratingParameter);
        ratingRepository.save(rating);
        return rating;
    }
```

Nous utilisons aussi la librairie pour mapper les champs des entités à nos DTO.

    public List<RatingDTO> getAllRatings() {
        return ratingRepository.findAll()
                .stream()
                .map(ratingMapper::toRatingDTO)
                .toList();
    }

Et utiliser des DTO pour afficher les données nécessaires aux utilisateurs.

```
    @RequestMapping("/rating/list")
    public String home(Model model) {
        List<RatingDTO> ratingDTOs = ratingService.getAllRatings();
        model.addAttribute("ratingDTOs", ratingDTOs);
        return "rating/list";
    }
```

## Sécurité

Nous avons créé un package security comportant deux classes.

*SpringSecurityConfig* 

→ Permet de paramétrer la chaîne de sécurité 

→ Déclare un Bean BCryptPasswordEncoder afin de hasher les mots de passes des utilisateurs 

→ Permet l'authentification des utilisateurs par session 

*CustomUserDetailsService* 

→ Permet de charger les utilisateurs de la base de données en fonction de leur username et de les convertir en objets UserDetails.

## Externalisation des identifiants de connection à la base de données

Grâce à l'utilisation de variables d'environnement, nous évitons d'avoir les identifiants de connection à la
base de données écrits en dur dans notre fichier application.properties.

## Galerie

Default Spring Security sign in form providing Session based Auth

![Default Spring Security sign in form providing Session based Auth](https://imgur.com/vKwimDs.jpeg)

Default Spring Security sign out form invalidating the Session
![Default Spring Security sign out form invalidating the Session](https://imgur.com/UKdfq9K.jpeg)

User Home
![User Home](https://imgur.com/opIFzZH.jpeg)

Rating List
![Rating List](https://imgur.com/RDchsSk.jpeg)

Updating a rating
![Updating a Rating](https://imgur.com/xqSijWK.jpeg)

Validation Error
![Validation Error](https://imgur.com/yl4PlzF.jpeg)

Access denied - Endpoint restricted to Admins
![Access denied - Endpoint restricted to Admins](https://imgur.com/EBhn4TL.jpeg)

Admin Home with additional user management section
![Alt text](https://imgur.com/L3v8cVN.jpeg)

Restricted User Management Section
![Restricted User Management Section](https://imgur.com/4CLqX8G.jpeg)

Hashed Passwords in DB
![Hashed Passwords in DB](https://imgur.com/Ixow6bz.jpeg)
