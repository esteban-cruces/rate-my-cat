/*
 * (C) Copyright 2017 Boni Garcia (https://bonigarcia.github.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.github.bonigarcia.test.unit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEmptyString.emptyString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import io.github.bonigarcia.Cat;
import io.github.bonigarcia.CatException;
import io.github.bonigarcia.CatRepository;
import io.github.bonigarcia.CatService;
import io.github.bonigarcia.Opinion;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (black-box): rating cats")
@Tag("unit")
class RateCatsTest {

    @InjectMocks
    CatService catService;

    @Mock
    CatRepository catRepository;
    

    // Test data
    Cat dummy = new Cat("dummy", "dummy.png");
    int stars = 5;
    String comment = "foo";


    @ParameterizedTest(name = "Rating cat with {0} stars")
    @ValueSource(doubles = { 0.5, 5 })
    @DisplayName("Correct range of stars test")
    @Tag("functional-requirement-3")
    void testCorrectRangeOfStars(double stars) {
        Cat dummyCat = catService.rateCat(stars, dummy);
        assertThat(dummyCat.getAverageRate(), equalTo(stars));
    }

    @ParameterizedTest(name = "Rating cat with {0} stars")
    @ValueSource(ints = { 0, 6 })
    @DisplayName("Incorrect range of stars test")
    @Tag("functional-requirement-3")
    void testIncorrectRangeOfStars(int stars) {
        assertThrows(CatException.class, () -> {
            catService.rateCat(stars, dummy);
        });
    }

    @Test
    @DisplayName("Rating cats with a comment")
    @Tag("functional-requirement-4")
    void testRatingWithComments() {
        when(catRepository.findById(any(Long.class))).thenReturn(Optional.of(dummy));

        Cat dummyCat = catService.rateCat(stars, comment, 0);
        assertThat(
                catService.getOpinions(dummyCat).iterator().next().getComment(),
                equalTo(comment));
    }


    @Test
    @DisplayName("Rating cats with empty comment")
    @Tag("functional-requirement-4")
    void testRatingWithEmptyComments() {
        when(catRepository.findById(any(Long.class))).thenReturn(Optional.of(dummy));

        Cat dummyCat = catService.rateCat(stars, dummy);
        assertThat(
                catService.getOpinions(dummyCat).iterator().next().getComment(),
                emptyString());
    }




    @DisplayName("Check when opinions is empty")
    @Test
    void testGetOpinions(){
        //lista que retorna getOpinions
        List<Opinion> opiniones = new ArrayList<>();
        opiniones.add(new Opinion(2.3, "esto es una prueba"));
        opiniones.add(new Opinion(4.5, "ya me aburrí"));

        //se crea gato de prueba
        Cat cat_1 = new Cat("1", "azula.png");
        cat_1.rate(2.3 , "esto es una prueba");
        cat_1.rate(4.5 , "ya me aburrí");
        List<Opinion> listOpinion = cat_1.getOpinions();
        
        assertEquals(listOpinion.get(0).getStars(), opiniones.get(0).getStars());
        assertEquals(listOpinion.get(0).getComment(), opiniones.get(0).getComment());

        assertEquals(listOpinion.get(1).getStars(), opiniones.get(1).getStars());
        assertEquals(listOpinion.get(1).getComment(), opiniones.get(1).getComment());
        
    }

    




}
