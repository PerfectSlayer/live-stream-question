const speakers = [
    {
        name: "Andy Damevin",
        avatar: "https://pbs.twimg.com/profile_images/449603516512735232/8qvErrfu_400x400.jpeg",
        contact: "@Ia3andy",
        title: ""
    },
    {
        name: "Clément Escoffier",
        avatar: "https://pbs.twimg.com/profile_images/1059135799147540480/AXaLqunC_400x400.jpg",
        contact: "@clementplop",
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