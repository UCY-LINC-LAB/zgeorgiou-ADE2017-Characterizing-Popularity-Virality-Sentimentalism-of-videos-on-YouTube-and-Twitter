var data;
var classification;

$(document).ready(function(){


    $("#graph-option").click(function(){
        var option = $("#graph-option option:selected").val();
        if(option =="Features"){
            $("#class").show();
        }else{
            $("#class").hide();
        }

        console.log('cliked');

    });

    //Load data Information
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

    $.getJSON("./static/data/classification.json", function(result, status){
            classification = result.data;
            console.log(classification);
            classification.popular_youtube.evaluation.all_old

    });

});



function getRow(key,value){
    var k = $("<td></td>").text(key);
    var v = $("<td></td>").text(value);
    var row = $('<tr></tr>').append(k)
    row.append(v);
    return row;
}



function graph(e){
    var option = $("#graph-option option:selected").val();
    if(option =="Area Under Curve"){
        buildAuc('graph');
    }else if(option =="Features"){
        var classz = $("#class-option option:selected").val();
        classz = classz.toLowerCase();
        buildFeatures('graph',classz);
    }
    //$("#auc-graph").append(buildAuc(option));
    return false;
}
function buildFeatures(id,classz){
    var myChart = Highcharts.chart(id, {
        chart: {
            type: 'column'
        },
        title: {
            text: 'Features Importance for ' + classz
        },
        xAxis: {
            categories: populateFeaturesLabels(classz,'all_old',10),
            title: {
                text: 'Features'
            }
        },
        yAxis: {
            title: {
                text: 'Percentage'
            }
        },
        series: [
            { 
                name: 'Features',
                data: populateFeaturesValues(classz,'all_old',10)
            },
        ]
    });
    return 
}

function populateFeaturesLabels(classz,key,num){
    var obj =   classification[classz].features_importance[key];
    var lbls = Object.keys(obj).map(function (key) { return key; });
    lbls = lbls.slice(0,num);
    console.log(lbls);
    return lbls;
}
function populateFeaturesValues(classz,key,num){
    var obj =   classification[classz].features_importance[key];
    var vals = Object.keys(obj).map(function (key) { return obj[key]; });
    vals = vals.slice(0,num);
    console.log(vals);
    return vals;
}

function buildAuc(id){
    var myChart = Highcharts.chart(id, {
        chart: {
            type: 'bar'
        },
        title: {
            text: 'Auc\'s'
        },
        xAxis: {
            categories: [
                'Popular-YouTube', 'Popular-Twitter', 'Popular-Both',
                'Viral-YouTube', 'Viral-Twitter', 'Viral-Both',
                'Popular-Viral-YouTube', 'Popular-Viral-Twitter', 'Popular-Viral-Both',
            ],
            title: {
                text: 'Classifiers'
            }
        },
        yAxis: {
            title: {
                text: 'Values'
            }
        },
        series: [
            { 
                name: 'All Features Old',
                data: getClassificationValues('all_old')
            },
            { 
                name: 'Baseline Features Old',
                data: getClassificationValues('baseline_old'), visible : false
            },
            { 
                name: 'All Features Recent',
                data: getClassificationValues('all_recent'),
            },
            { 
                name: 'Baseline Features Recent',
                data: getClassificationValues('baseline_recent'), visible : false
            },
        ]
    });
    return 
}

function getClassificationValues(key){
    return [
        classification.popular_youtube.evaluation[key],
        classification.popular_twitter.evaluation[key],
        classification.popular_both.evaluation[key],
        classification.viral_youtube.evaluation[key],
        classification.viral_twitter.evaluation[key],
        classification.viral_both.evaluation[key],
        classification.popular_viral_youtube.evaluation[key],
        classification.popular_viral_twitter.evaluation[key],
        classification.popular_viral_both.evaluation[key],
        ]
}
