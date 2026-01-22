@Entity
@Table(name = "shows")
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer showid;

    private Integer cinemaid;
    private Integer hallid;
    private Integer movieid;
    private Integer availableseats;

    private LocalDateTime starttime;
}