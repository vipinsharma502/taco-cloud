package com.example.tacos.data;

import com.example.tacos.Taco;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

@RequiredArgsConstructor
@Repository
public class JdbcTacoRepository  implements TacoRepository{

    private final JdbcTemplate jdbcTemplate;
    private final IngredientRepository ingredientRepository;

    @Override
    public Taco save(Taco taco) {
        long keyId = saveToTacoInfo(taco);
        taco.setId(keyId);
        for(String ingredient: taco.getIngredients()){
            saveIngredientToTaco(ingredient, taco.getId());
        }
        return taco;
    }

    private void saveIngredientToTaco(String ingredient, Long id) {
        jdbcTemplate.update("insert into Taco_Ingredients (taco, ingredient) values(?,?)", id, ingredient);
    }

    private Long saveToTacoInfo(Taco taco) {
        taco.setCreatedAt(new Date());
        PreparedStatementCreatorFactory preparedStatementCreatorFactory = new PreparedStatementCreatorFactory("insert into Taco(name, createdAt) values (?,?)",Types.VARCHAR, Types.TIMESTAMP);
        preparedStatementCreatorFactory.setReturnGeneratedKeys(true);
        PreparedStatementCreator psc = preparedStatementCreatorFactory.newPreparedStatementCreator(Arrays.asList(taco.getName(),new Timestamp(taco.getCreatedAt().getTime())));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc,keyHolder);
        return keyHolder.getKey().longValue();
    }
}
