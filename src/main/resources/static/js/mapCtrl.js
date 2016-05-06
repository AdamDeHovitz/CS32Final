bulkAppControllers.controller("mapCtrl", 
	function($scope, $rootScope, $http, $stateParams, $state, $timeout, $interval) {

	var eventId = $stateParams.eventId;
	$.post("/event-view", {id: eventId}, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			$scope.event = responseObject;
	});

});