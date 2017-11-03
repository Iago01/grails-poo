package senha


class Usuario{
    String nome;
    String senha;
static hasMany = [senhas:Senha]

    static constraints = {
    }
}
