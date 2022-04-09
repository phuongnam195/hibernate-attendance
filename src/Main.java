import dao.SubjectDAO;
import entity.Subject;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Subject> subjects = SubjectDAO.getListSubject();
        for (int i = 0; i < subjects.size(); i++) {
            System.out.println(subjects.get(i).getName());
        }
    }
}
