// Look up the Web Speech API (https://developer.mozilla.org/en-US/docs/Web/API/SpeechSynthesis)
// Initialize this variable when the window first loads
let VOICE_SYNTH: SpeechSynthesis;

// The current speaking rate of the screen reader
let VOICE_RATE: number = 1;

// Stores elements and their handler functions
// Think of an appropriate data structure to do this
// Assign this variable in mapPage()
let ELEMENT_HANDLERS;

// Indicates the current element that the user is on
// You can decide the type of this variable
let current;
let PAUSED: boolean = false;


class elementHandler {
    elt: Element;
    handler: Function

    constructor(elt: Element, handler: Function) {
        this.elt = elt;
        this.handler = handler;
    }
}

/**
 * Speaks out text.
 * @param text the text to speak
 */
function speak(text: string): void {
    if (VOICE_SYNTH) {
        // initialize a speech request using SpeechSynthesisUtterance
        var utterance = new SpeechSynthesisUtterance(text);
        utterance.rate = VOICE_RATE;

        VOICE_SYNTH.speak(utterance);

        // calls async func and waits for onend - if called several times
        utterance.onstart = async function () { // pauses
            await utterance.onend;
            return;
        }
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

    document.addEventListener("keydown", globalKeystrokes);
}

function generateHandlers(): void {
    const collection = document.getElementsByTagName("*");

    let count = 0;
    for (let elt of collection) {
        elt.setAttribute('id',count.toString()); // sets unique numerical ID
        count+=1;
    }

    ELEMENT_HANDLERS = Array<elementHandler>();
    for (let elt of collection) {
       ELEMENT_HANDLERS.push(new elementHandler(elt, handlers(elt)));
    }
}

// to use: next week
function highlight(text: string, elt: Element): void{
    var inputText = elt.innerHTML

    var index = inputText.indexOf(text);
    if (index >= 0) {
        inputText = inputText.substring(0, index) + "<span class='highlight'>" + inputText.substring(index, index + text.length) + "</span>" + inputText.substring(index + text.length);
        elt.innerHTML = inputText;
    }
    // var element = <HTMLElement> elt;
    // element.style.backgroundColor = '2px solid yellow';
}

function handlers(elt: Element): Function {
    if (elt.tagName === "TITLE") { // metadata
        return function() {
            speak("Title:");
            highlight(elt.innerHTML, elt); // highlight element
            speak(elt.innerHTML);
        }
    }
    else if (elt.tagName === "H1" || elt.tagName === "H2" || elt.tagName === "H3"
        || elt.tagName === "H4" || elt.tagName === "H5" || elt.tagName === "H6" || elt.tagName === "P") { //text
        return function() {
            speak("Header:");
            highlight(elt.innerHTML, elt); // highlight element
            speak(elt.innerHTML);
        }
    }
    else if (elt.tagName === "IMG") { // graphics
        var imageElt = <HTMLImageElement> elt;
        return function() {
            speak("Graphic:");
            if (imageElt.alt != null) { // if there is a caption
                highlight(imageElt.alt, imageElt);
                speak(imageElt.alt);
            }
        }
    }
    else if (elt.tagName === "A") { // interactive
        return function() {
            speak("URL:");
            highlight(elt.innerHTML, elt);
            speak(elt.innerHTML);
            }
        }
    else if (elt.tagName === "LABEL") { // interactive : reads out label for an input form
        return function() {
            highlight(elt.innerHTML, elt);
            speak(elt.innerHTML);
        }
    }
    else if (elt.tagName === "INPUT") { // interactive
        return function() {
            speak("Input:")
            highlight(elt.innerHTML, elt);
            speak(elt.innerHTML);
        }
    }
    else if (elt.tagName === "BUTTON") { // interactive
        return function() {
            speak("Button:")
            highlight(elt.innerHTML, elt);
            speak(elt.innerHTML);
        }
    }
    else if (elt.tagName === "TABLE") { // tables
        var tableElt = <HTMLTableElement> elt;
        return function() {
            speak("Table:");
            highlight(elt.innerHTML, elt); // TODO: fix how tables are highlighted, and non-text objects
        }
    }
    else if (elt.tagName === "CAPTION") { // tables
        return function() {
            speak("Caption:");
            highlight(elt.innerHTML, elt);
            speak(elt.innerHTML);
        }
    }
    else if (elt.tagName === "TD") { // tables
        return function() {
            highlight(elt.innerHTML, elt);
            speak(elt.innerHTML);
        }
    }
    else if (elt.tagName === "TFOOT") { // tables
        return function() {
            speak("Summarizing column:"); // TODO: fix how it is announced
            highlight(elt.innerHTML, elt);
            speak(elt.innerHTML);
        }
    }
    else if (elt.tagName === "TH") { // tables
        return function() {
            speak("Header:");
            highlight(elt.innerHTML, elt);
            speak(elt.innerHTML);
        }
    }
    else if (elt.tagName === "TR") { // tables
        return function() {
            speak("Row:");
            highlight(elt.innerHTML, elt);
            speak(elt.innerHTML);
        }
    }
    return function(){};
}

/**
 * Changes the speaking rate of the screen reader.
 * @param factor multiplier on the speaking rate
 */
function changeVoiceRate(factor: number): void {
    VOICE_RATE *= factor
    if (VOICE_RATE > 4) {
        VOICE_RATE = 4;
    } else if (VOICE_RATE < 0.25){
        VOICE_RATE = 0.25;
    }
}

/**
 * Moves to the next HTML element in the DOM.
 */
function next() {}

/**
 * Moves to the previous HTML element in the DOM.
 */
function previous() {


}

/**
 * Starts reading the page continuously.
 */
function start() {


}

/**
 * Pauses the reading of the page.
 */
 function pause() {
     PAUSED = true;
}

 /**
 * Resumes the reading of the page.
 */
  function resume() {
      PAUSED = false;
 }

/**
 * Listens for keydown events.
 * @param event keydown event
 */
function globalKeystrokes(event: KeyboardEvent): void {
    // can change and add key mappings as needed
    if (event.key === " ") {
        event.preventDefault();
        //TODO: start reading the entire page
    } else if (event.key === "ArrowRight") {
        event.preventDefault();
        changeVoiceRate(1.1);
    } else if (event.key === "ArrowLeft") {
        event.preventDefault();
        changeVoiceRate(0.9);
    } 
}
