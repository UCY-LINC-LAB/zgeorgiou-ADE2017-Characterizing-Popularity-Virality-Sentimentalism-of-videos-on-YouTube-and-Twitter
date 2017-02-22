export class VideoCategories {

    private options = {};

    constructor(data : any){
        let title = { text : 'Categories' };
        let tooltip = { pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>' };
        let chart = { type: 'pie', plotShadow: true };
        let plotOptions = { 
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.y}',
                },
                showInLegend: true
        }
        };
        let points = [];
        for(let category of data.categories){
            points.push({name:category['name'], y:category['total_videos']});
        }

        let series = [{
            name: 'Videos',
            colorByPoint: true,
            data: points
        }];
        this.options['title'] =title;
        this.options['tooltip'] =tooltip;
        this.options['chart'] =chart;
        this.options['plotOptions'] =plotOptions;
        this.options['series'] =series;
    }


    getGraph() {
        return this.options;
    }
}
