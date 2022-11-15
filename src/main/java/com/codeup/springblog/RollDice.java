package com.codeup.springblog;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class RollDice {

    @GetMapping("/roll-dice")
    public String showRollDiceLinks() {
        return "roll-dice";
    }

    @GetMapping("/roll-dice/{guess}")
    public String rollDiceGuess(@PathVariable int guess, Model model) {
        ArrayList<Integer> diceRolls = new ArrayList<>();
        int correctGuesses = 0;
        int diceRoll;
        for(int i = 0; i < 10; i++) {
            diceRoll = ThreadLocalRandom.current().nextInt(1, 7);
            if(diceRoll == guess) {
                correctGuesses++;
            }
            diceRolls.add(diceRoll);
        }
        model.addAttribute("diceRolls", diceRolls);
        model.addAttribute("guess", guess);
        model.addAttribute("correctGuesses", correctGuesses);
        return "dice-guess";
    }
}
