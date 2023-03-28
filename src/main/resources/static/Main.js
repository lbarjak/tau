import GraphFrame from './GraphFrame.js'

export default class Main {

    constructor() {
        this.graphFrame = new GraphFrame()
    }

    main = () => {

        let indexOfMeasuredTemperatures = data.indexOfMeasuredTemperatures

        window.ctx = graph.getContext('2d')

        ctx.fillStyle = 'lightgray'
        ctx.fillRect(0, 0, width, height)
        ctx.font = fontSize + "px 'Inconsolata'"
        ctx.fontStretch = "condensed";
        ctx.textAlign = "end"

        ctx.transform(0.9, 0, 0, -0.8, width * 0.07, height * 0.83)

        let graphIndoor = () => {
            let previousX
            let previousY
            let x
            let y
            let colorOn = false
            let color
            color = "rgba(0, 0, 0, 0)"
            for (let i = 0; i < temperatures.length; i++) {
                x = i * width / temperatures.length
                y = (height / this.graphFrame.yTicks) * (temperatures[i]["roomTemp"] - this.graphFrame.yTickMin)
                if (colorOn) {
                    color = i <= indexOfMeasuredTemperatures ? "blue" : "red"
                }
                this.graphFrame.drawLine(ctx, [previousX, previousY], [x, y], color, 4)
                if (temperatures[i]["roomTemp"] != null) {
                    colorOn = true
                }
                previousX = x
                previousY = y
            }
        }

        let graphOutdoor = () => {
            let color
            let colorSwitch = true
            let previousX = 0
            let previousY = (height / this.graphFrame.yTicks) * (temperatures[0]["outdoorTemp"] - this.graphFrame.yTickMin)
            let x
            let y
            for (let i = 0; i < temperatures.length; i++) {
                x = i * width / temperatures.length
                y = (height / this.graphFrame.yTicks) * (temperatures[i]["outdoorTemp"] - this.graphFrame.yTickMin)
                colorSwitch = true
                if (temperatures[i]["outdoorTemp"] == null) {
                    color = "rgba(0, 0, 0, 0)"
                    colorSwitch = false
                }
                this.graphFrame.drawLine(ctx, [previousX, previousY], [x, y], color, 3)
                if (colorSwitch) {
                    color = "navy"
                }
                previousX = x
                previousY = y
            }
        }

        this.graphFrame.xTicks()
        this.graphFrame.yTicks()
        graphOutdoor()
        graphIndoor()

    }
}
