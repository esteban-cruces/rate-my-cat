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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import java.util.Optional;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.bonigarcia.Cat;
import io.github.bonigarcia.CookiesService;
import io.github.bonigarcia.Opinion;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (white-box): handling cookies")
@Tag("unit")
@Tag("functional-requirement-5")
class CookiesTest {

    @InjectMocks
    CookiesService cookiesService;

    @Mock
    HttpServletResponse response;
    

    // Test data
    Cat dummy = new Cat("dummy", "dummy.png");
    String dummyCookie = "0#0.0#_";

    @Test
    @DisplayName("Update cookies test")
    void testUpdateCookies() {
        doNothing().when(response).addCookie(any(Cookie.class));
        String cookies = cookiesService.updateCookies("", 0L, 0D, "", response);
        assertThat(cookies, containsString(CookiesService.VALUE_SEPARATOR));
        assertThat(cookies, containsString(CookiesService.CAT_SEPARATOR));
    }

    @Test
    @DisplayName("Check cat in cookies")
    void testCheckCatInCookies() {
        boolean catInCookies = cookiesService.isCatInCookies(dummy,
                dummyCookie);
        assertThat(catInCookies, equalTo(true));
    }

    @DisplayName("Check cat in empty cookies")
    @Test
    void testCheckCatInEmptyCookies() {
        boolean catInCookies = cookiesService.isCatInCookies(dummy, "");
        assertThat(catInCookies, equalTo(false));
    }

    @DisplayName("Update opinions with cookies")
    @Test
    void testUpdateOpinionsWithCookies() {
        List<Opinion> opinions = cookiesService
                .updateOpinionsWithCookiesValue(dummy, dummyCookie);
        assertThat(opinions, not(empty()));
    }

    @DisplayName("Update opinions with empty cookies")
    @Test
    void testUpdateOpinionsWithEmptyCookies() {
        List<Opinion> opinions = cookiesService
                .updateOpinionsWithCookiesValue(dummy, "");
        assertThat(opinions, empty());
    }



    //test is OpinionInCookies
    Opinion dummy2 = new Opinion( 3.4 , "hola que tal");

    @Test
    @DisplayName("Check opinion when is true")
    void testCheckOpinionInCookies() {
        boolean opinionInCookies = cookiesService.isOpinionInCookies(dummy2, 3.4,
                "hola que tal");
        assertThat(opinionInCookies, equalTo(true));
    }

    @DisplayName("Check Opinion when stars is not equal")
    @Test
    void testCheckOpinionInStarsEmptyCookies() {
        boolean catInCookies = cookiesService.isOpinionInCookies(dummy2, 0 ,"hola que tal");
        assertThat(catInCookies, equalTo(false));
    }

    @DisplayName("Check Opinion when comment is not equal")
    @Test
    void testCheckOpinionInCommentEmptyCookies() {
        boolean catInCookies = cookiesService.isOpinionInCookies(dummy2, 3.4 ,"");
        assertThat(catInCookies, equalTo(false));
    }

    //creando lista de Cat
    //este objeto Cat se genera con ID 0
    Cat cat_1 = new Cat("carlina", "azula.png");
    
    String exampleCookie = "0#3.4#_22#3.4#_";
    String exampleCookie2 = "22#3.4#_11#3.5#_";
    
    
 

    @DisplayName("Check cat value is inside of cookie")
    @Test
    void checkGetValueForCat(){
        cat_1.rate(3.4 , "esto es una prueba");
        Optional<String> salida = cookiesService.getValueForCat(cat_1, exampleCookie);
        assertEquals(salida , Optional.of("0#3.4#"));
    }

    @DisplayName("Check cat value is not inside of cookie")
    @Test
    void checkGetValueForCatEmpty(){
        cat_1.rate(3.4 , "esto es una prueba");
        Optional<String> salida = cookiesService.getValueForCat(cat_1, exampleCookie2);
        assertEquals(salida , Optional.empty());
    }

}
