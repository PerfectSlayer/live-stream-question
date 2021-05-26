const speakers = [
    {
        name: "Lilian Benoit",
        avatar: "https://pbs.twimg.com/profile_images/378800000064899867/1c623b76a82a2aa044f684136a028cf6_400x400.png",
        contact: "@Lilian_Benoit",
        title: ""
    }
]

function loadSpeaker(speaker) {
    $('.avatar').attr('src', speaker.avatar);
    $('.name').text(speaker.name);
    $('.contact').text(speaker.contact);
    $('.title').text(speaker.title);
}

function getSpeakerFromLocation() {
    const hast = window.location.hash;
    const index = hast ? hast.substr(1) : 0;
    return speakers[index];
}

window.onload = function () {
    loadSpeaker(getSpeakerFromLocation());
}