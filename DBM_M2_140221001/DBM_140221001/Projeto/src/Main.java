import M2M.XMLTOModel;
import M2T.Java;
import M2TBd.Mysql;

//import person.Person;
import M2TBd.Sqlite3;
import Web.Web;
import Web.PureHTML;
import proj.person.Manel;
import proj.person.Person;
import proj.person.Super;
import utils.Build;


import java.io.File;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {


        Build build = new Build(new Java(), new XMLTOModel("src/model/person.xml"), new Sqlite3(), new PureHTML());
        build.build(new File("C:\\Users\\tiago\\Desktop\\my2"));



    }

    public static void testORM() {

        //  Person p = Person.get(1);


        //  Manel m = p.getmanels();

        // System.out.println(m);

        //System.out.println("msa".contains("a"));
        //System.out.println(Manel.get(15));

        Person p = new Person();
        p.setOk(5);
        p.setName("Alberto");
        p.setAge(22);
        p.setDa(new Date());
        p.save();

        Person p2 = new Person();
        p2.setOk(2);
        p2.setName("Alberto");
        p2.setAge(22);
        p2.setDa(new Date());
        p2.save();

        Manel m = new Manel();
        m.setOk(1);
        m.setName("name");
        m.setAge(22);
        m.setCoi("abc123");
        m.setMan(21);
        m.setDa(new Date());
        m.save();

        Manel m2 = new Manel();
        m2.setOk(2);
        m2.setName("name");
        m2.setAge(22);
        m2.setCoi("manel");
        m2.setMan(21);
        m2.setDa(new Date());
        m2.save();

        m.addPerson(p);
        m.addPerson(p2);
        m2.addPerson(p);
        m2.addPerson(p2);


        List<Super> test = Super.all();

        for (int k = 0; k < test.size(); k++) {
            System.out.println(test.get(k));
        }

        /*



        Manel m = new Manel();
        m.setName("manel");
        m.setAge(21);
        m.save();

        System.out.println(p.getManel());

        p.setManel(m);
        p2.setManel(m);


        System.out.println(p.getManel());

*/
        System.out.println();
        List<Super> persons = Person.all();
        System.out.println(persons.size());
        for (int i = 0; i < persons.size(); i++) {
            System.out.println(persons.get(i));
        }

    }

    public static void test() {

        //herança

        /*
        Aluno al = new Aluno(1, new Date());
        al.setNome("Tiago");
        al.setDataNascimento(new Date());
        al.save();
        al.setNome("Manel");
        al.save();

        Professor p = new Professor();
        p.setGabinete("13");
        p.setNome("olaa");
        p.setNumero(1);
        p.setDataNascimento(new Date());
        p.save();


        List<Utilizador> utilizadors = Utilizador.all();

        for(Utilizador ut : utilizadors){
            System.out.println(ut);
        }

        */
        /*
        one to one
        */
        /*
        System.out.println();
        System.out.println("One to One TEST");
        al.setProfessor(p);
        System.out.println(p.getAluno());
        */


        //oneToMany
        /*
        Aluno al2 = new Aluno(1, new Date());
        al2.setNome("Tiago");
        al2.setDataNascimento(new Date());
        al2.save();

        al.setProfessor(p);
        al2.setProfessor(p);

        List<Aluno> alunos = p.getAlunos();

        for(Aluno a : alunos){
            System.out.println(a);
        }
        */

        //ManyToMany

        /*
        Aluno al2 = new Aluno(1, new Date());
        al2.setNome("Tiago");
        al2.setDataNascimento(new Date());
        al2.save();

        Professor p2 = new Professor();
        p2.setGabinete("13");
        p2.setNome("olaa");
        p2.setNumero(1);
        p2.setDataNascimento(new Date());
        p2.save();

        al.addProfessor(p);
        al.addProfessor(p2);
        al2.addProfessor(p);
        al2.addProfessor(p2);

        System.out.println();

        System.out.println("--------- PROFS DO AL ---------");

        List<Professor> professors = al.getProfessors();
        for(Professor pp : professors){
            System.out.println(pp);
        }

        System.out.println("--------- PROFS DO AL2 ---------");
        professors = al2.getProfessors();
        for(Professor pp : professors){
            System.out.println(pp);
        }

        System.out.println("-------- ALunos p ----------");

        List<Aluno> alunos = p.getAlunos();

        for(Aluno a : alunos){
            System.out.println(a);
        }*/
    }

}


