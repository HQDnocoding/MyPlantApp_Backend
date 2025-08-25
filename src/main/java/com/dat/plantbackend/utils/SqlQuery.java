package com.dat.plantbackend.utils;

public class SqlQuery {
    public static class Treatment {


        public static final String FIND_TREATMENT_BY_DISEASE_NAME_AND_PLANT =
                "SELECT " + "  t.id, " +
                        "  t.doseperacre, " +
                        "  t.instruction, " +
                        "  d.name AS disease_name, " +
                        "  p.name AS pesticide_name, " +
                        "  p.description AS pesticide_des, " +
                        " t.disease_description " +
                        "FROM Treatment t " +
                        "INNER JOIN Disease d ON t.disease_id = d.id " +
                        "INNER JOIN Pesticide p ON t.pesticide_id = p.id " +
                        "INNER JOIN Plant pl ON t.plant_id = pl.id " +
                        "WHERE d.name ILIKE :disease AND pl.name ILIKE :plant";


        public static final String GET_TREATMENTS = """
                    SELECT t.* FROM treatment t
                    LEFT JOIN disease d ON t.disease_id = d.id
                    LEFT JOIN pesticide p ON t.pesticide_id = p.id
                    LEFT JOIN plant pl ON t.plant_id = pl.id
                    WHERE (:diseaseName IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :diseaseName, '%')))
                      AND (:pesticideName IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :pesticideName, '%')))
                      AND (:plantName IS NULL OR LOWER(pl.name) LIKE LOWER(CONCAT('%', :plantName, '%')))
                      AND (:dose IS NULL OR LOWER(t.doseperacre) LIKE LOWER(CONCAT('%', :dose, '%')))
                    ORDER BY t.id ASC
                """;
    }

    public static class History {

        public static final String FIND_HISTORY_BY_USER_ID =
                "SELECT * FROM History WHERE user_id = :userId";
        public static final String COUNT_HISTORY_BY_USER_ID =
                "SELECT COUNT(*) FROM History WHERE user_id = :userId";

        public static final String FIND_HISTORY_BY_ID =
                "SELECT * FROM History WHERE id = :id";

        public static final String DELETE_HISTORY_BY_ID =
                "DELETE FROM History WHERE id = :id";
    }

    public static class User{
        public static final String FIND_USER_BY_EMAIL_AND_TYPE_ACCOUNT =
               "SELECT * FROM \"user\" WHERE username = :email AND type_account = :typeAccount LIMIT 1";
    }
}

