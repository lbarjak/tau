export default class Main {

    main = () => {

        ctx.fillStyle = 'lightgray'
        ctx.fillRect(0, 0, width, height)
        ctx.font = fontSize + "px 'Inconsolata'"
        ctx.fontStretch = "condensed";
        ctx.textAlign = "end"

        ctx.transform(0.9, 0, 0, -0.8, width * 0.07, height * 0.83)

    }
}
