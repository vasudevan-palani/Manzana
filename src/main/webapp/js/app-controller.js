minionModule.controller('SearchController', function($scope, $rootScope, $utils,$state) {

	$scope.data={};

	$scope.floatTheadOptions = {
	    scrollingTop: 60,
	    useAbsolutePositioning: false
	};

	$scope.envNames=[{"id":'clfysci',"name":"clfysci"},{"id":'clfysit1',"name":"clfysit1"}];

	$scope.getQueryNames = function(){

		$utils.ajax(URL+'/getQueryNames', {}, function(data) {
			$scope.queryNames = data.object;
		});
	}
	$scope.names=[];
	$scope.addName = function(){
		$scope.names.push({name:$scope.iName,value:$scope.iValue});
	}

	$scope.deleteName = function(index){
		$scope.names.splice(index,1);
	}

	$scope.results=[];
	$scope.selectQuery=function(query){
		console.log($scope.data);
		$utils.ajax(URL+'/execQuery', {env:$scope.data.env.id,queryId:query.id,params:$scope.names}, function(data) {
			$scope.updateResults(query,data);
		});
	}

	$scope.updateResults=function(query,data){
		var flag = false;
		for(index in $scope.results){
			if($scope.results[index].query.id == query.id){
				flag = true;
				$scope.results[index] = {query:query,columns:data.columns,rows:data.rows};
			}
		}
		if(flag == false){
			$scope.results.push({query:query,columns:data.columns,rows:data.rows});
		}
	}

	$scope.deleteResult=function(index){
		$scope.results.splice(index,1);
	}
	$scope.updateResult=function(query){
		$scope.selectQuery(query);
	}

	$scope.copyValue=function(val1){
		$scope.iValue = val1;
	}

	$scope.editQuery = function(query){
		$rootScope.editQueryStage = query;
		$state.go('edit');
	}

});

minionModule.controller('AddController', function($scope, $rootScope, $utils,$state) {

	$scope.addQuery=function(){
		$utils.ajax(URL+'/addQuery', $scope.data, function(data) {

		});
	}
});

minionModule.controller('EditController', function($scope, $rootScope, $utils,$state) {

	$scope.loadQuery = function(){
		var query = $rootScope.editQueryStage;
		$utils.ajax(URL+'/getQuery', {queryId:query.id}, function(data) {

		});		
	}

	$scope.updateQuery=function(){
		$utils.ajax(URL+'/updateQuery', $scope.data, function(data) {

		});
	}
});