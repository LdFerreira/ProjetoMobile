package leandro.br.com.projetomobile.Entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Aluno on 26/06/2018.
 */

@DatabaseTable
public class Shopping {

    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private Integer id;

    @DatabaseField(canBeNull = false)
    String nome;

    @DatabaseField(canBeNull = true)
    String endereco;

    @DatabaseField(canBeNull = true)
    Double rating;

    public Shopping(String nome) {
        this.nome = nome;
    }

    public Shopping() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return nome;
    }
}
