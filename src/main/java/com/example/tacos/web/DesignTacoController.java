package com.example.tacos.web;

import com.example.tacos.Ingredient;
import com.example.tacos.Order;
import com.example.tacos.Taco;
import com.example.tacos.data.IngredientRepository;
import com.example.tacos.data.TacoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.example.tacos.Ingredient.Type;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

    private final IngredientRepository ingredientRepository;
    private final TacoRepository designRepository;

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @ModelAttribute(name="order")
    public Order order() {
        return new Order();
    }

    @GetMapping
    public String showDesignForm(Model model) {
        List<Ingredient> ingredients = StreamSupport.stream(ingredientRepository.findAll().spliterator(), false).collect(Collectors.toList());
        Type[] types = Type.values();
        for(Type type: types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }

        model.addAttribute("design", new Taco());
        return "design";
    }

    @PostMapping
    public String processDesign(@Valid  @ModelAttribute("design") Taco design, Errors errors, @ModelAttribute("order") Order order) {
        if(errors.hasErrors()) {
            return "design";
        }
        log.info("Processing design:" + design);
        Taco saved = designRepository.save(design);
        order.addDesign(saved);
        return "redirect:/orders/current";
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients.stream().filter(ingredient -> ingredient.getType().equals(type))
                .collect(Collectors.toList());
    }
}
