var data;
var classification;

$(document).ready(function(){


    $("#graph-option").click(function(){
        var option = $("#graph-option option:selected").val();
        if(option =="Features1"){
            $("#class-features1").show();
            $("#class-features2").hide();
        }else if(option =='Features2'){
            $("#class-features1").hide();
            $("#class-features2").show();
        }else{
            $("#class-features1").hide();
            $("#class-features2").hide();
        }
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
    }else if(option =="Features1"){
        var classz = $("#class-features1-option option:selected").val();
        classz = classz.toLowerCase();
        var key = $("#key-features1-option option:selected").val();
        key = key.toLowerCase();
        buildFeatures1('graph',key,classz);
    }else if(option =="Features2"){
        var classz = $("#class-features2-option option:selected").val();
        classz = classz.toLowerCase();
        var key = $("#key-features2-option option:selected").val();
        key = key.toLowerCase();
        buildFeatures2('graph',classz,key);
    }
    return false;
}
function buildFeatures1(id,key,classz){
    var myChart = Highcharts.chart(id, {
        chart: {
            type: 'column'
        },
        title: {
            text: 'Features Importance for ' + classz
        },
        xAxis: {
            categories: populateFeaturesLabels(classz,key,10),
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
                data: populateFeaturesValues(classz,key,10)
            },
        ]
    });
}

function populateFeaturesLabels(classz,key,num){
    var obj =   classification[classz].features_importance[key];
    var lbls = Object.keys(obj).map(function (key) { return key; });
    lbls = lbls.slice(0,num);
    return lbls;
}
function populateFeaturesValues(classz,key,num){
    var obj =   classification[classz].features_importance[key];
    var vals = Object.keys(obj).map(function (key) { return obj[key]; });
    vals = vals.slice(0,num);
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


function buildFeatures2(id,classz,key){
    var series = getAllFeaturesSeries(classz,key);
    var myChart = Highcharts.chart(id, {
        chart: {
            type: 'bar'
        },
        title: {
            text: 'Features'
        },
        xAxis: {
            categories: getAllFeaturesCategories(classz) ,
            title: {
                text: 'Features'
            }
        },
        yAxis: {
            title: {
                text: 'Percentage'
            }
        },
        series: series
    });
}
function getAllFeaturesCategories(classz){
    if(classz.endsWith("youtube")){
        return ['Views_2','Likes_2','Views_1','Likes_1','Video_duration'];
    }
    if(classz.endsWith("twitter")){
        return ['User_Followers','User_Friends']
    }
}

function getAllFeaturesSeries(classz,key){
    if(classz == "youtube"){
        var py = getAllFeatures('popular_youtube', key);
        var vy = getAllFeatures('viral_youtube', key);
        var pvy = getAllFeatures('popular_viral_youtube', key);
        var t = [{ name: 'Popular-YouTube', data: py },
            { name: 'Viral-YouTube', data: vy},
            { name: 'Popular-Viral-YouTube', data: pvy}
        ];
        return t;
    }
    if(classz == "twitter"){
        var py = getAllFeatures('popular_twitter', key);
        var vy = getAllFeatures('viral_twitter', key);
        var pvy = getAllFeatures('popular_viral_twitter', key);
        var t = [{ name: 'Popular-Twitter', data: py },
            { name: 'Viral-Twitter', data: vy},
            { name: 'Popular-Viral-Twitter', data: pvy}
        ];
        return t;
    }
    return 1;
}
function getAllFeatures(classz,key){
    if(classz.endsWith("youtube")){
        var x= [ 
            classification[classz].features_importance[key].views_2,
            classification[classz].features_importance[key].likes_2,
            classification[classz].features_importance[key].views_1,
            classification[classz].features_importance[key].likes_1,
            classification[classz].features_importance[key].video_duration,
        ];
        return x;
    }
    if(classz.endsWith("twitter")){
        var x= [ 
            classification[classz].features_importance[key].tw_user_followers,
            classification[classz].features_importance[key].tw_user_friends,
        ];
        return x;
    }
}