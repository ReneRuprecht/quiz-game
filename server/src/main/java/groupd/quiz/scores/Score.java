package groupd.quiz.scores;

import com.fasterxml.jackson.annotation.JsonIgnore;
import groupd.quiz.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@Entity(name = "Score")
@Table(
        name = "score"

)
public class Score {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;


    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id",
            referencedColumnName = "user_id",
            foreignKey = @ForeignKey(name = "Fk_user_id"))
    @MapsId
    private User user;

    @Transient
    private int currentScoreValue = 0;

    @JsonIgnore
    @Column(
            name = "total_score",
            nullable = false

    )
    private double totalScore;

    @Column(
            name = "games_played",
            nullable = false

    )
    private int gamesPlayed;


    @Column(
            name = "win",
            nullable = false

    )
    private int win;
    @Column(
            name = "lose",
            nullable = false

    )
    private int lose;

    @JsonIgnore
    @Column(
            name = "avg",
            nullable = false

    )
    private double avg;

    public Score() {
        this.win = 0;
        this.lose = 0;
        this.avg = 0;
    }


}
