bulkAppControllers.controller("membersCtrl", 
	function($scope, $rootScope, $http, $stateParams, $state, $timeout, $interval) {

	var eventId = $stateParams.eventId;

	var getMembers = function() {
		$.post("/user-members", {"id": eventId}, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			$scope.requests = responseObject.requests;
        });
	}

	//$timeout(getRequests, 0);
	//$scope.gettingReqs = $interval(getRequests, 5000);
	/*
	if ($state.current.name == "tab.events-requests") {
		$scope.curProfileUrl = "events-profile";
	} else {
		$scope.curProfileUrl = "profile";
	}*/
	
	$scope.goProfile = function(id) {
		if ($state.current.name == "tab.events-requests") {
	 		$state.go('tab.events-profile',{userId: id});
	 	} else {
		 	$state.go('tab.profile',{userId: id});
		}
	};
});