var data;

$(document).ready(function(){


    $.getJSON("./static/data/data.json", function(result, status){
            data = result.data;
            console.log(data);
            var table = $('<table class="table table-striped"></table>');
            table.append('<tr><th>Key</th><th>Value</th></tr>');

            table.append(getRow('Experiment Id',data.experiment_id));
            table.append(getRow('Category',data.category_name));
            table.append(getRow('Number of Videos',data.number_of_videos));
            table.append(getRow('Percentage used',(Number(data.percentage) * 100)+'%'));
            table.append(getRow('Train window',data.train_wnd));
            table.append(getRow('Offset window',data.offset));
            table.append(getRow('Labeling window',data.lbl_wnd));
            $('#experiment').append(table);

        });

});



function getRow(key,value){
    var k = $("<td></td>").text(key);
    var v = $("<td></td>").text(value);
    var row = $('<tr></tr>').append(k)
    row.append(v);
    return row;
}

/**
$(function () { 
    var myChart = Highcharts.chart('container', {
        chart: {
            type: 'bar'
        },
        title: {
            text: 'Fruit Consumption'
        },
        xAxis: {
            categories: ['Apples', 'Bananas', 'Oranges']
        },
        yAxis: {
            title: {
                text: 'Fruit eaten'
            }
        },
        series: [{
            name: 'Jane',
            data: [1, 0, 4]
        }, {
            name: 'John',
            data: [5, 7, 3]
        }]
    });
});
**/