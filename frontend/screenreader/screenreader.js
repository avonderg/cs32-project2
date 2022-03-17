"use strict";
// Look up the Web Speech API (https://developer.mozilla.org/en-US/docs/Web/API/SpeechSynthesis)
// Initialize this variable when the window first loads
let VOICE_SYNTH;
// The current speaking rate of the screen reader
let VOICE_RATE = 1;
// Stores elements and their handler functions
// Think of an appropriate data structure to do this
// Assign this variable in mapPage()
let ELEMENT_HANDLERS = {};
// Indicates the current element that the user is on
// You can decide the type of this variable
let current; // corresponds to ID of the element
/**
 * Speaks out text.
 * @param text the text to speak
 */
function speak(text) {
    if (VOICE_SYNTH) {
        // initialize a speech request using SpeechSynthesisUtterance
        var utterance = new SpeechSynthesisUtterance(text);
        utterance.rate = VOICE_RATE;
        // listens for pause
        utterance.addEventListener('pause', function (event) {
            console.log('Speech paused after ' + event.elapsedTime + ' milliseconds.');
        });
        VOICE_SYNTH.speak(utterance);
        // begins to speak
        console.log("Rate: " + utterance.rate);
        return new Promise((resolve) => {
            window.setInterval(() => {
                if (!VOICE_SYNTH.speaking) {
                    resolve();
                }
            }, 100);
        });
    }
}
window.onload = () => {
    generateHandlers();
    document.body.innerHTML = `
        <div id="screenReader">
            <button>Start [Space]</button>
            <button>Pause/Resume [P]</button>
            <button onclick="changeVoiceRate(1.1);">Speed Up [Right Arrow]</button>
            <button onclick="changeVoiceRate(0.9);">Slow Down [Left Arrow]</button>
        </div>
    ` + document.body.innerHTML;
    VOICE_SYNTH = window.speechSynthesis;
    // need to get the buttons
    // screen reader button click events
    const buttons = document.getElementById("screenReader").getElementsByTagName("button");
    buttons[0].addEventListener("click", (event) => start("0"));
    buttons[1].addEventListener("click", function () {
        if (VOICE_SYNTH.paused) {
            resume();
        }
        else {
            pause();
        }
    });
    buttons[2].addEventListener("click", (event) => changeVoiceRate(1.1));
    buttons[3].addEventListener("click", (event) => changeVoiceRate(0.1));
    document.addEventListener("keydown", globalKeystrokes);
};
/**
 * Gets all the elements by their tagnames, sets their ID's, and inserts each element into the global
 * ELEMENT_HANDLERS array along with their handler functions
 */
function generateHandlers() {
    // gets HTML elements
    const collection = document.getElementsByTagName("*");
    // iterates through all DOM elements
    for (var i = 0; i < collection.length; i++) {
        const htmlElt = collection[i];
        let textTags = ["P", "H1", "H2", "H3", "H4", "H5", "H6", "LABEL", "TITLE", "CAPTION", "TH", "TD"];
        let tableTags = ["TABLE", "CAPTION", "TD", "TFOOT", "TH", "TR"];
        // assigns an id to each element
        htmlElt.id = String(i);
        // stores elements and corresponding handlers in global ELEMENT_HANDLERS
        if (textTags.indexOf(htmlElt.tagName) > -1) {
            ELEMENT_HANDLERS[i] = [htmlElt, (x) => pureTextHandlers(x)];
        }
        else if (htmlElt.tagName == "IMG") {
            ELEMENT_HANDLERS[i] = [htmlElt, (x) => imgHandlers(x)];
        }
        else if (htmlElt.tagName == "A") {
            ELEMENT_HANDLERS[i] = [htmlElt, (x) => linkHandlers(x)];
        }
        else if (htmlElt.tagName == "INPUT") {
            ELEMENT_HANDLERS[i] = [htmlElt, (x) => inputHandlers(x)];
        }
        else if (tableTags.indexOf(htmlElt.tagName) > -1) {
            ELEMENT_HANDLERS[i] = [htmlElt, (x) => tableHandlers(x)];
        }
    }
}
/**
 * Generates handler functions for text elements
 * @param elt: HTMLElement input
 */
function pureTextHandlers(elt) {
    if (elt.tagName == "TITLE") {
        speak("Title :" + elt.textContent);
    }
    else if (elt.tagName == "LABEL") {
        speak("Label :" + elt.textContent);
    }
    else {
        speak(elt.textContent);
    }
}
/**
 * Generates handler functions for image elements
 * @param elt: HTMLElement input
 */
function imgHandlers(elt) {
    speak(elt.alt);
}
/**
 * Generates handler functions for input elements
 * @param elt: HTMLElement input
 */
function inputHandlers(elt) {
    speak("There is an input of type \""
        + elt.type + " \" here. Click enter to interact with it.");
    document.body.addEventListener("keyup", function (event) {
        // Number 13 is the "Enter" key on the keyboard
        if (event.key === "enter") {
            // Cancel the default action, if needed
            event.preventDefault();
            // Trigger the button element with a click
            resume();
        }
    });
    pause();
}
/**
 * Generates handler functions for link elements
 * @param elt: HTMLElement input
 */
function linkHandlers(elt) {
    document.body.addEventListener("keyup", function (event) {
        // Number 13 is the "Enter" key on the keyboard
        if (event.key === "enter") {
            // Cancel the default action, if needed
            event.preventDefault();
            // Trigger the button element with a click
            window.open(elt.href);
        }
    });
    speak("link: " + elt.textContent +
        "Click enter to open the link. Click r to resume");
}
/**
 * Generates handler functions for table elements
 * @param elt:  HTMLElement input
 */
function tableHandlers(elt) {
    if (elt.tagName == "CAPTION" || elt.tagName == "TH" || elt.tagName === "TD") {
        if (elt.children.length < 0) {
            speak(elt.textContent);
        }
    }
}
/**
 * Function highlighting input HTML elements, to help partially-blind users recognize which section
 * is currently being read by the screen reader
 * @param text - text to highlight
 * @param elt - current element
 */
function highlight(elt) {
    // // var inputText = elt.innerHTML
    // //
    // // var index = inputText.indexOf(text);
    // // if (index >= 0) {
    // //     inputText = inputText.substring(0, index) + "<span class='highlight'>" + inputText.substring(index, index + text.length) + "</span>" + inputText.substring(index + text.length);
    // //     elt.innerHTML = inputText;
    // // }
    // var element = <HTMLElement> elt;
    // element.style.backgroundColor = '2px solid yellow';
    // return;
    var body = document.getElementById("BODY");
    var bodyColor;
    if (body != null) {
        bodyColor = body.style.color;
    }
    // change style of previously selected value
    var prev = document.getElementById(String(+elt.id - 1));
    if (prev != null) {
        prev.style.background = bodyColor || "";
        prev.style.color = "#000";
    }
    var curr = document.getElementById(elt.id);
    if (curr != null) {
        curr.style.background = "#0098b2";
        curr.style.color = "fff";
    }
}
/**
 * Changes the speaking rate of the screen reader.
 * @param factor multiplier on the speaking rate
 */
function changeVoiceRate(factor) {
    VOICE_RATE *= factor;
    if (VOICE_RATE > 4) {
        VOICE_RATE = 4;
    }
    else if (VOICE_RATE < 0.25) {
        VOICE_RATE = 0.25;
    }
}
/**
 * Moves to the next HTML element in the DOM.
 */
function next() {
    VOICE_SYNTH.cancel();
    const next = +current + 1;
    const nextElt = ELEMENT_HANDLERS[next];
    if (nextElt != null) {
        current = String(next);
        start(String(next));
    }
}
/**
 * Moves to the previous HTML element in the DOM.
 */
function previous() {
    VOICE_SYNTH.cancel();
    const prev = +current - 1;
    const prevElt = ELEMENT_HANDLERS[prev];
    if (prevElt != null) {
        current = String(prev);
        start(String(prev));
    }
}
/**
 * Starts reading the page continuously.
 */
function start(curr) {
    for (const elt in ELEMENT_HANDLERS) {
        if (curr <= elt) {
            current = elt;
            const curElt = ELEMENT_HANDLERS[elt];
            highlight(curElt[0]);
            curElt[1](curElt[0]);
        }
    }
}
/**
 * Pauses the reading of the page.
 */
function pause() {
    VOICE_SYNTH.pause();
}
/**
* Resumes the reading of the page.
*/
function resume() {
    VOICE_SYNTH.resume();
}
/**
 * Listens for keydown events.
 * @param event keydown event
 */
function globalKeystrokes(event) {
    // can change and add key mappings as needed
    if (event.key === " ") {
        event.preventDefault();
        start("0");
    }
    else if (event.key === "ArrowRight") {
        event.preventDefault();
        changeVoiceRate(1.1);
    }
    else if (event.key === "ArrowLeft") {
        event.preventDefault();
        changeVoiceRate(0.9);
    }
    else if (event.key === "p") {
        if (VOICE_SYNTH.paused) { // if it is paused, then resume
            resume();
        }
        else { // otherwise, pause
            pause();
        }
    }
    else if (event.key == "ArrowUp") {
        previous();
    }
    else if (event.key == "ArrowDown") {
        next();
    }
}
