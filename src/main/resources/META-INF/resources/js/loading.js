const start = Date.now();
const end = new Date().setHours(19, 0, 0, 0); // Today at 7pm
const steps = 30;
const stepDuration = (end - start) / steps;

const remainingTimeSpan = document.getElementById('remainingTime');
const progressDiv = document.getElementById('progress');
const actionDiv = document.getElementById('actions');
const drawings = [
    '░', '▒', '▓', '█'
];
let lastDrawing = drawings.length -1;

const prefixes = [
  '[<span class="level lightblue">DEBUG</span>]',
  '[<span class="level darkblue">INFO</span>]',
  '[<span class="level warning">WARNING</span>]',
  '[<span class="level error">ERROR</span>]',
];
const sentences = [
    'Télécharge tout l\'Internet avec mvn package 🌍',
    'Augmente &#8209;Xmx à l\'avance 🤫',
    'Définit &#8209;Xms mais pas sûr que le live démarre plus vite 🤷‍',
    'Ajoute &#8209;XX:+HeapDumpOnOutOfMemoryError au cas où quelque chose se passe mal 🧯',
    'Quel est le &#8209;XX:HeapDumpPath par défaut au fait? 🤔',
    'Où est donc la Javadoc de la classe classe AbstractAnnotationConfigDispatcherServletInitializer ? 📚',
    'Quand je reçois 0, ça fait true ou false ? 🧐',
    'Ca tourne en root mais c\'est dans un conteneur ! 🔒',
    'HTTP 725: ça marche sur ma machine 😎',
    'Peut-être qu\'avec un petit F5 de plus ça marche ? 🔄',
    'Est-ce qu\'on a pensé à vider le cache ? 🗑️',
    'Comment on augmente le PermGenSpace déjà ? 📈',
    'Concours Twitter: 3 licences IntelliJ à gagner ! 🎁',
    'Concours Twitter: une photo et les 3 handles @kanedafromparis @parisjug pour participer 🎉'
];

function updateTime() {
    const now = Date.now();
    const remainingMinutes = Math.ceil((end - now) / 1000 / 60);

    let remaining;
    if (remainingMinutes > 1) {
        remaining = remainingMinutes + ' minutes :';
    } else if (remainingMinutes === 1) {
        remaining = '1 minute :';
    } else {
        remaining = 'un bref instant !';
    }
    remainingTimeSpan.innerText = remaining;
}

function drawLoading() {
    const now = Date.now();
    const past = now - start;
    const pastSteps = Math.floor(past / stepDuration);

    let progress = "[";
    for (let i = 0; i<steps; i++) {
        if (i < pastSteps) {
            progress+= '█';
        } else if (i === pastSteps) {
            lastDrawing = (lastDrawing+1) % drawings.length;
            progress+= drawings[lastDrawing];
        } else {
            progress+= '&nbsp;';
        }
    }
    progress+= ']';

    progressDiv.innerHTML = progress;
}

function updateAction() {
    const prefix = prefixes[Math.floor(Math.random() * prefixes.length)];
    const sentence = sentences[Math.floor(Math.random() * sentences.length)];
    actionDiv.innerHTML = prefix+' '+sentence;
}

window.onload = _ => {
    window.setInterval(updateTime, 500);
    window.setInterval(drawLoading, 250);
    window.setInterval(updateAction, 10_000);
}