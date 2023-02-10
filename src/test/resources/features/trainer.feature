Feature: Lingo Trainer
  As a Lingo player
  I would like to practice guessing words
  So that I can improve at Lingo

  Scenario: Starting a new game
    When I start a new game
    Then I should get a 5 letter word to guess
    And I should see the first letter of this word

  Scenario Outline: Game continues after guessing a word
    Given we are playing a game
    And the word to guess is <current length> letters long
    When I successfully guess the word
    Then the next round should start
    And the word to guess is <next length> letter long

    Examples:
    | current length | next length |
    | 5              | 6           |
    | 6              | 7           |
    | 7              | 5           |

  Scenario Outline: Attempting to guess a word
    Given we are playing a game
    And the word to guess is <word to guess>
    When I attempt <attempt>
    Then I should see <feedback>

    Examples:
    | word to guess | attempt | feedback                                    |
    | groep         | gegroet | INVALID, INVALID, INVALID, INVALID, INVALID |
    | groep         | gerst   | CORRECT, PRESENT, PRESENT, ABSENT, ABSENT   |
    | groep         | genen   | CORRECT, ABSENT, ABSENT, CORRECT, ABSENT    |
    | groep         | gedoe   | CORRECT, PRESENT, ABSENT, PRESENT, ABSENT   |
    | groep         | groep   | CORRECT, CORRECT, CORRECT, CORRECT, CORRECT |